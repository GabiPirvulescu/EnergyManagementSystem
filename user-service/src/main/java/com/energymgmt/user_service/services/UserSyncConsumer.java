package com.energymgmt.user_service.services;

import com.energymgmt.user_service.config.RabbitMQConfig;
import com.energymgmt.user_service.dtos.UserSyncDTO;
import com.energymgmt.user_service.entities.User;
import com.energymgmt.user_service.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserSyncConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSyncConsumer.class);
    private final UserRepository userRepository;

    public UserSyncConsumer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void consumeUserSync(UserSyncDTO message) {
        LOGGER.info("Received sync message: {}", message.getAction());

        if ("CREATE".equals(message.getAction())) {
            User user = new User();
            user.setID(message.getId());
            user.setUsername(message.getUsername());
            user.setName(message.getName() != null ? message.getName() : "Unknown");
            user.setAddress(message.getAddress() != null ? message.getAddress() : "Unknown");
            user.setAge(message.getAge() != null ? message.getAge() : 0);
            user.setPassword("PROTECTED");

            userRepository.save(user);
            LOGGER.info("User profile created for ID: {}", message.getId());
        }
        else if ("DELETE".equals(message.getAction())) {
            if (userRepository.existsById(message.getId())) {
                userRepository.deleteById(message.getId());
                LOGGER.info("User profile deleted for ID: {}", message.getId());
            }
        }
    }
}