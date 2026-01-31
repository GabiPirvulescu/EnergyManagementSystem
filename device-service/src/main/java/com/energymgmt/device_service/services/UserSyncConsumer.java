package com.energymgmt.device_service.services;

import com.energymgmt.device_service.config.RabbitMQConfig;
import com.energymgmt.device_service.dtos.UserSyncDTO;
import com.energymgmt.device_service.entities.UserReference;
import com.energymgmt.device_service.repositories.UserReferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserSyncConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncConsumer.class);
    private final UserReferenceRepository userReferenceRepository;

    public UserSyncConsumer(UserReferenceRepository userReferenceRepository) {
        this.userReferenceRepository = userReferenceRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.DEVICE_USER_QUEUE)
    public void consumeUserSync(UserSyncDTO message) {
        LOGGER.info("Received User Sync Message: {} for ID {}", message.getAction(), message.getId());

        try {
            if ("CREATE".equals(message.getAction())) {
                userReferenceRepository.save(new UserReference(message.getId()));
                LOGGER.info("User ID {} synced to Device DB", message.getId());
            }
            else if ("DELETE".equals(message.getAction())) {
                if (userReferenceRepository.existsById(message.getId())) {
                    userReferenceRepository.deleteById(message.getId());
                    LOGGER.info("User ID {} removed from Device DB", message.getId());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to sync user: {}", e.getMessage());
        }
    }
}