package com.energymgmt.user_service.services;

import com.energymgmt.user_service.config.RabbitMQConfig;
import com.energymgmt.user_service.dtos.UserDTO;
import com.energymgmt.user_service.dtos.UserDetailsDTO;
import com.energymgmt.user_service.dtos.UserSyncDTO;
import com.energymgmt.user_service.dtos.builders.UserBuilder;
import com.energymgmt.user_service.entities.User;
import com.energymgmt.user_service.repositories.UserRepository;
import com.energymgmt.user_service.handlers.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    public static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate; 

    @Autowired
    public UserService(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    private User findUserByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.error("User with id {} not found.", id);
                    return new ResourceNotFoundException("User with id " + id + " not found.");
                });
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAllUsers(){
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserBuilder::toUserDTO).collect(Collectors.toList());
    }

    @Transactional
    public UUID insertUser(UserDetailsDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);

        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getID());
        return user.getID();
    }

    @Transactional
    public UserDetailsDTO updateUser(UUID id, UserDetailsDTO userDetailsDTO) {
        User existingUser = findUserByIdOrThrow(id);

        existingUser.setName(userDetailsDTO.getName());
        existingUser.setAddress(userDetailsDTO.getAddress());
        existingUser.setAge(userDetailsDTO.getAge());

        existingUser = userRepository.save(existingUser);
        LOGGER.debug("User with id {} was updated", existingUser.getID());

        return UserBuilder.toUserDetailsDTO(existingUser);
    }

    @Transactional
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            LOGGER.error("User with id {} not found.", id);
            throw new ResourceNotFoundException("User with id " + id + " not found.");
        }
        userRepository.deleteById(id);

        try {
            UserSyncDTO syncData = new UserSyncDTO(id, "DELETE");
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "user.deleted", syncData);
        } catch (Exception e) {
            LOGGER.error("Failed to send sync message to RabbitMQ", e);
        }

        LOGGER.debug("User with id {} was deleted", id);
    }
}