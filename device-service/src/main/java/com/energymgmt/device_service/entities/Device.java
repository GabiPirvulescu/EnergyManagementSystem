package com.energymgmt.device_service.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_hourly_consumption", nullable = false)
    private Double maxHourlyConsumption;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private UUID userId;

    public Device() {
    }

    public Device(UUID id, String name, Double maxHourlyConsumption, String description, UUID userId) {
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