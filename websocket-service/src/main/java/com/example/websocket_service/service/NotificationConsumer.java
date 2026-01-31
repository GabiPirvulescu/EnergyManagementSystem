package com.example.websocket_service.service;

import com.example.websocket_service.dtos.NotificationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class NotificationConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "notification_queue")
    public void consumeNotification(Message message) {
        try {
            byte[] body = message.getBody();

            String jsonPayload = new String(body, StandardCharsets.UTF_8);

            NotificationDTO notification = objectMapper.readValue(
                    jsonPayload,
                    NotificationDTO.class
            );

            System.out.println("Received notification for user: " + notification.getUserId());

            String destination = "/topic/alerts/" + notification.getUserId();

            messagingTemplate.convertAndSend(destination, notification);

        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            System.err.println("Failed to parse notification JSON: " + e.getMessage());
            System.err.println("Raw payload: " + new String(message.getBody(), StandardCharsets.UTF_8));

        } catch (Exception e) {
            System.err.println("Error processing notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
}