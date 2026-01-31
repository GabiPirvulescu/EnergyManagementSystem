package com.energymgmt.device_simulator.simulator;

import com.energymgmt.device_simulator.dto.SensorDataDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class SimulationRunner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private static final long TEN_MINUTES_IN_MS = 600000;

    private final List<UUID> deviceIds = Arrays.asList(
            UUID.fromString("af16f494-b7a8-4088-a1b6-35bc17afefab"),
            UUID.fromString("8df2cba2-bca8-4fd0-b699-0fe7d1f9a35f"),
            UUID.fromString("541b3047-c391-4f55-b8ef-dc570c50e5b3")
    );

    public SimulationRunner(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> STARTING SIMULATOR FOR " + deviceIds.size() + " DEVICES");

        Random random = new Random();
        long currentSimulatedTime = System.currentTimeMillis();

        while (true) {
            for (UUID deviceId : deviceIds) {

                LocalDateTime simulatedDateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(currentSimulatedTime),
                        ZoneId.systemDefault()
                );
                int hour = simulatedDateTime.getHour();

                double measurementValue;
                double noise = (random.nextDouble() * 2.0) - 1.0;

                if (hour >= 7 && hour < 19) {
                    measurementValue = 15.0 + (noise * 4.0);
                } else {
                    measurementValue = 50.0 + (noise * 0.5);
                }

                if (measurementValue < 0.1) measurementValue = 0.1;

                SensorDataDTO data = new SensorDataDTO(currentSimulatedTime, deviceId, measurementValue);

                try {
                    rabbitTemplate.convertAndSend("sensor-data-queue", data);
                    System.out.println(String.format(" [x] Sent for %s: %.2f kWh", deviceId.toString().substring(0, 8), measurementValue));
                } catch (Exception e) {
                    System.err.println("❌ Error sending: " + e.getMessage());
                }

                Thread.sleep(500);
            }

            currentSimulatedTime += TEN_MINUTES_IN_MS;
            System.out.println("--- 10 Mins Passed ---");
            Thread.sleep(2000);
        }
    }
}