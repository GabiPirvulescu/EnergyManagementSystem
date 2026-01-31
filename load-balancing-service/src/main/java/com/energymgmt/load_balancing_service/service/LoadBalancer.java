package com.energymgmt.load_balancing_service.service;

import com.energymgmt.load_balancing_service.dto.SensorDataDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class LoadBalancer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final int REPLICA_COUNT = 3;

    @RabbitListener(queues = "sensor-data-queue")
    public void distributeTraffic(String messageJson) {
        try {
            SensorDataDTO data = objectMapper.readValue(messageJson, SensorDataDTO.class);

            int index = Math.abs(data.getDeviceId().hashCode()) % REPLICA_COUNT;

            String targetQueue = "ingest_queue_" + index;

            System.out.println("Load Balancer: Device " + data.getDeviceId().toString().substring(0,8)
                    + " -> " + targetQueue);

            rabbitTemplate.convertAndSend(targetQueue, messageJson);

        } catch (Exception e) {
            System.err.println("❌ Error processing message: " + e.getMessage());
        }
    }
}