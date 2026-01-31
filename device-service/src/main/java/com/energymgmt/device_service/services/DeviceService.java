package com.energymgmt.device_service.services;

import com.energymgmt.device_service.dtos.DeviceDTO;
import com.energymgmt.device_service.dtos.DeviceDetailsDTO;
import com.energymgmt.device_service.dtos.DeviceSyncDTO;
import com.energymgmt.device_service.dtos.builders.DeviceBuilder;
import com.energymgmt.device_service.entities.Device;
import com.energymgmt.device_service.handlers.ResourceNotFoundException;
import com.energymgmt.device_service.repositories.DeviceRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import com.energymgmt.device_service.config.RabbitMQConfig;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, RabbitTemplate rabbitTemplate) {
        this.deviceRepository = deviceRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    private Device findDeviceByIdOrThrow(UUID id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("Device with id {} not found.", id);
                    return new ResourceNotFoundException("Device with id " + id + " not found.");
                });
    }

    @Transactional(readOnly = true)
    public List<DeviceDTO> findAllDevicesForUser(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_ADMINISTRATOR"));

        List<Device> deviceList;

        if (isAdmin) {
            LOGGER.debug("Preluare toate dispozitivele pentru ADMIN");
            deviceList = deviceRepository.findAll();
        } else {
            UUID userId = UUID.fromString(authentication.getPrincipal().toString());
            LOGGER.debug("Preluare dispozitive pentru CLIENT ID: {}", userId);
            deviceList = deviceRepository.findByUserId(userId);
        }

        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public DeviceDetailsDTO findDeviceById(UUID id) {
        Device device = findDeviceByIdOrThrow(id);
        return DeviceBuilder.toDeviceDetailsDTO(device);
    }

    @Transactional
    public UUID insertDevice(DeviceDetailsDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);
        device = deviceRepository.save(device);

        DeviceSyncDTO syncData = new DeviceSyncDTO(device.getId(), device.getMaxHourlyConsumption(), device.getUserId(), "CREATE");
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "device.created", syncData);
        LOGGER.info("Message sent to RabbitMQ for device creation: {}", device.getId());

        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return device.getId();
    }

    @Transactional
    public DeviceDetailsDTO updateDevice(UUID id, DeviceDetailsDTO deviceDTO) {
        Device existingDevice = findDeviceByIdOrThrow(id);

        existingDevice.setName(deviceDTO.getName());
        existingDevice.setDescription(deviceDTO.getDescription());
        existingDevice.setMaxHourlyConsumption(deviceDTO.getMaxHourlyConsumption());
        existingDevice.setUserId(deviceDTO.getUserId());

        Device savedDevice = deviceRepository.save(existingDevice);
        LOGGER.debug("Device with id {} was updated in db", savedDevice.getId());

        DeviceSyncDTO syncData = new DeviceSyncDTO(
                savedDevice.getId(),
                savedDevice.getMaxHourlyConsumption(),
                savedDevice.getUserId(),
                "UPDATE"
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "device.updated", syncData);


        return DeviceBuilder.toDeviceDetailsDTO(existingDevice);
    }

    @Transactional
    public void deleteDevice(UUID id) {
        if (!deviceRepository.existsById(id)) {
            LOGGER.error("Device with id {} not found.", id);
            throw new ResourceNotFoundException("Device with id " + id + " not found.");
        }
        Optional<Device> deviceToDelete = deviceRepository.findById(id);
        deviceRepository.deleteById(id);
        LOGGER.debug("Device with id {} was deleted from db", id);

        DeviceSyncDTO syncData = new DeviceSyncDTO(id, 0.0, deviceToDelete.get().getUserId(),  "DELETE");
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "device.deleted", syncData);
        LOGGER.info("Message sent to RabbitMQ for device deletion: {}", id);
    }

    @Transactional
    public DeviceDTO assignUserToDevice(UUID deviceId, UUID userId) {
        Device device = findDeviceByIdOrThrow(deviceId);
        device.setUserId(userId);
        Device savedDevice = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was assigned to user {}.", savedDevice.getId(), userId);

        DeviceSyncDTO syncData = new DeviceSyncDTO(
                savedDevice.getId(),
                savedDevice.getMaxHourlyConsumption(),
                savedDevice.getUserId(),
                "UPDATE"
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "device.updated", syncData);

        return DeviceBuilder.toDeviceDTO(savedDevice);
    }
}