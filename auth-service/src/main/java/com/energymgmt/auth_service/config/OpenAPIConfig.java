//package com.energymgmt.auth_service.config;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//
//@Configuration
//@OpenAPIDefinition(
//        info = @Info(title = "Energy Management API", version = "v1"),
//        security = @SecurityRequirement(name = "BearerAuth")
//)
//
//@SecurityScheme(
//        name = "BearerAuth",
//        type = SecuritySchemeType.HTTP,
//        scheme = "bearer",
//        bearerFormat = "JWT"
//)
//public class OpenAPIConfig {
//}