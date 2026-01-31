package com.energymgmt.auth_service.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.energymgmt.auth_service.entities.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;
    private static final long EXPIRATION_TIME_HOURS = 1;

    private Algorithm algorithm;

    private Algorithm getAlgorithm() {
        if (algorithm == null) {
            this.algorithm = Algorithm.HMAC256(secretKey);
        }
        return this.algorithm;
    }

    public String generateToken(UserCredentials user) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(EXPIRATION_TIME_HOURS, ChronoUnit.HOURS);

        return JWT.create()
                .withIssuer("auth-service")
                .withSubject(user.getUsername())
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expirationTime))
                .withClaim("role", user.getRole())
                .withClaim("userId", user.getId().toString())
                .sign(getAlgorithm());
    }

}