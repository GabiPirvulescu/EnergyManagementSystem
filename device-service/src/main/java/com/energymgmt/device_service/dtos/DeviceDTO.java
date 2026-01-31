package com.energymgmt.device_service.dtos;

import java.util.UUID;

public class DeviceDTO {

    private UUID id;
    private String name;
    private Double maxHourlyConsumption;
    private UUID userId;

    public DeviceDTO(UUID id, String name, Double maxHourlyConsumption, UUID userId) {
        this.id = id;
        this.name = name;
        this.maxHourlyConsumption = maxHourlyConsumption;
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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

}
