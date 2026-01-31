package com.energymgmt.auth_service.services;

import com.energymgmt.auth_service.builders.UserCredentialsBuilder;
import com.energymgmt.auth_service.config.RabbitMQConfig;
import com.energymgmt.auth_service.dtos.*;
import com.energymgmt.auth_service.entities.UserCredentials;
import com.energymgmt.auth_service.exceptions.UsernameAlreadyExistsException;
import com.energymgmt.auth_service.repository.UserCredentialsRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RabbitTemplate rabbitTemplate;

    public AuthService(UserCredentialsRepository userCredentialsRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       RabbitTemplate rabbitTemplate) {
        this.userCredentialsRepository = userCredentialsRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * register new user
     */
    @Transactional
    public UUID registerUser(RegisterDTO registerDTO) {
        UUID id = UUID.randomUUID();
        UserCredentials user = new UserCredentials();
        user.setId(id);
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(registerDTO.getRole().toUpperCase());

        userCredentialsRepository.save(user);
        UserSyncDTO syncData = new UserSyncDTO(
                id,
                registerDTO.getUsername(),
                registerDTO.getName(),
                registerDTO.getAddress(),
                registerDTO.getAge(),
                registerDTO.getRole(),
                "CREATE"
        );

        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "user.created", syncData);
        return id;
    }

    /**
     * login user and return jwt
     */
    public TokenDTO loginUser(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserCredentials credentials = userCredentialsRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        String token = jwtService.generateToken(credentials);

        return new TokenDTO(token);
    }
}