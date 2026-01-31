package com.energymgmt.monitoring_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "monitored_devices")
public class MonitoredDevice {

    @Id
    private UUID id;

    @Column(name = "max_hourly_consumption")
    private double maxHourlyConsumption;

    @Column(name = "user_id")
    private UUID userId;

    public MonitoredDevice() {
    }

    public MonitoredDevice(UUID id, double maxHourlyConsumption, UUID userId) {
        this.id = id;
        this.maxHourlyConsumption = maxHourlyConsumption;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getMaxHourlyConsumption() {
        return maxHourlyConsumption;
    }

    public void setMaxHourlyConsumption(double maxHourlyConsumption) {
        this.maxHourlyConsumption = maxHourlyConsumption;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}