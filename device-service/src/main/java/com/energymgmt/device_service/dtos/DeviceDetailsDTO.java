package com.energymgmt.device_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public class DeviceDetailsDTO {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Max Hourly Consumption is required")
    @Positive(message = "Max Hourly Consumption must be positive")
    private Double maxHourlyConsumption;

    private String description;
    private UUID userId;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(UUID id, String name, Double maxHourlyConsumption, String description, UUID userId) {
        this.id = id;
        this.name = name;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.description = description;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxHourlyConsumption() {
        return maxHourlyConsumption;
    }

    public void setMaxHourlyConsumption(Double maxHourlyConsumption) {
        this.maxHourlyConsumption = maxHourlyConsumption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
