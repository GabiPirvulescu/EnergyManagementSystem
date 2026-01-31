package com.energymgmt.auth_service.controllers;

import com.energymgmt.auth_service.dtos.LoginDTO;
import com.energymgmt.auth_service.dtos.RegisterDTO;
import com.energymgmt.auth_service.dtos.TokenDTO;
import com.energymgmt.auth_service.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * public endpoint: permitted by security config
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterDTO registerDTO) {
        UUID userId = authService.registerUser(registerDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("id", userId);
        response.put("message", "User registered successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    /**
     * public endpoint: permitted by security config
     */
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        TokenDTO token = authService.loginUser(loginDTO);
        return ResponseEntity.ok(token);
    }
}