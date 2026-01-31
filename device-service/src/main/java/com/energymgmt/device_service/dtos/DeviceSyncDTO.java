package com.energymgmt.device_service.dtos;

import java.io.Serializable;
import java.util.UUID;

public class DeviceSyncDTO implements Serializable {
    private UUID id;
    private double maxHourlyConsumption;
    private UUID userId;
    private String action;

    public DeviceSyncDTO(UUID id, double maxHourlyConsumption, UUID userId, String action) {
        this.id = id;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.userId = userId;
        this.action = action;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public double getMaxHourlyConsumption() { return maxHourlyConsumption; }
    public void setMaxHourlyConsumption(double val) { this.maxHourlyConsumption = val; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}