package com.energymgmt.user_service.dtos;

import com.energymgmt.user_service.dtos.validators.annotation.AgeLimit;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

public class UserDetailsDTO {
    private UUID id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "address is required")
    private String address;

    @NotNull(message = "Age is required")
    @AgeLimit(value = 18)
    private Integer age;

    public UUID getId() {
        return id;
    }

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(UUID id, String username, String password, String name, String address, Integer age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
