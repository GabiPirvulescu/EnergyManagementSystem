package com.energymgmt.monitoring_service.services;

import com.energymgmt.monitoring_service.config.RabbitMQConfig;
import com.energymgmt.monitoring_service.dtos.DeviceSyncDTO;
import com.energymgmt.monitoring_service.dtos.NotificationDTO;
import com.energymgmt.monitoring_service.dtos.SensorDataDTO;
import com.energymgmt.monitoring_service.entities.HourlyConsumption;
import com.energymgmt.monitoring_service.entities.MonitoredDevice;
import com.energymgmt.monitoring_service.entities.SensorMeasurement;
import com.energymgmt.monitoring_service.repository.MonitoredDeviceRepository;
import com.energymgmt.monitoring_service.repository.SensorMeasurementRepository;
import com.energymgmt.monitoring_service.repository.HourlyConsumptionRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumerService.class);

    private final MonitoredDeviceRepository deviceRepository;
    private final SensorMeasurementRepository measurementRepository;
    private final HourlyConsumptionRepository hourlyConsumptionRepository;
    private final RabbitTemplate rabbitTemplate;

    public MessageConsumerService(MonitoredDeviceRepository deviceRepo,
                                  SensorMeasurementRepository measureRepo,
                                  HourlyConsumptionRepository hourlyRepo,
                                  RabbitTemplate rabbitTemplate) {
        this.deviceRepository = deviceRepo;
        this.measurementRepository = measureRepo;
        this.hourlyConsumptionRepository = hourlyRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.MONITORING_DEVICE_QUEUE)
    public void consumeDeviceSync(DeviceSyncDTO message) {
        LOGGER.info("Sync message received: {} for device {}", message.getAction(), message.getId());

        if ("CREATE".equals(message.getAction()) || "UPDATE".equals(message.getAction())) {
            MonitoredDevice device = new MonitoredDevice(
                    message.getId(),
                    message.getMaxHourlyConsumption(),
                    message.getUserId()
            );
            deviceRepository.save(device);
            LOGGER.info("✅ Device synced: {}", device.getId());
        } else if ("DELETE".equals(message.getAction())) {
            if(deviceRepository.existsById(message.getId())) {
                deviceRepository.deleteById(message.getId());
                LOGGER.info("✅ Device deleted: {}", message.getId());
            }
        }
    }

    @RabbitListener(queues = {"ingest_queue_0", "ingest_queue_1", "ingest_queue_2"})
    public void consumeSensorData(SensorDataDTO data) {
        LOGGER.info("📊 Sensor data received for device: {}", data.getDeviceId());

        MonitoredDevice device = deviceRepository.findById(data.getDeviceId()).orElse(null);

        if (device == null) {
            LOGGER.warn("⚠️ Unknown device ID: {}", data.getDeviceId());
            return;
        }

        SensorMeasurement measurement = new SensorMeasurement(
                data.getTimestamp(),
                data.getMeasurementValue(),
                data.getDeviceId()
        );
        measurementRepository.save(measurement);

        long hourTimestamp = (data.getTimestamp() / 3600000) * 3600000;

        HourlyConsumption hourly = hourlyConsumptionRepository
                .findByDeviceIdAndTimestamp(data.getDeviceId(), hourTimestamp)
                .orElse(new HourlyConsumption(data.getDeviceId(), hourTimestamp, 0.0));

        double newTotal = hourly.getTotalConsumption() + data.getMeasurementValue();
        hourly.setTotalConsumption(newTotal);

        hourlyConsumptionRepository.save(hourly);

        LOGGER.info("✅ Hourly consumption for device {} at hour {}: {}",
                data.getDeviceId(), hourTimestamp, newTotal);

        if (newTotal > device.getMaxHourlyConsumption()) {
            LOGGER.warn(" OVERCONSUMPTION detected for device {}", device.getId());
            LOGGER.warn(" Max Limit: {}", device.getMaxHourlyConsumption());
            LOGGER.warn("  Current Total: {}", newTotal);

            NotificationDTO alert = new NotificationDTO(
                    "High energy usage detected! Device: " + device.getId() +
                            "Consumption: " + String.format("%.2f", newTotal) + " kWh" +
                            " Limit: " + device.getMaxHourlyConsumption() + " kWh",
                    device.getUserId(),
                    device.getId(),
                    System.currentTimeMillis()
            );

            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, alert);
            LOGGER.info("Alert sent to notification queue for user {}", device.getUserId());
        }
    }
}