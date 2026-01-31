package com.energymgmt.device_service.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.verifier = JWT.require(algorithm)
                .withIssuer("auth-service")
                .build();
    }

    /**
     * Validează un token și returnează un obiect de autentificare
     */
    public Authentication validateToken(String token) {
        DecodedJWT decodedJwt = verifier.verify(token);

        String role = decodedJwt.getClaim("role").asString();
        String userId = decodedJwt.getClaim("userId").asString();

        if (userId == null || userId.isEmpty()) {
            throw new RuntimeException("Token invalid: revendicarea (claim) userId lipsește sau este goală");
        }

        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
}
