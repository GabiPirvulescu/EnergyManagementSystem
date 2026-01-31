package com.energymgmt.monitoring_service.entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "measurements")
public class SensorMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private long timestamp;
    private double measurementValue;
    private UUID deviceId;

    public SensorMeasurement() {}

    public SensorMeasurement(long timestamp, double measurementValue, UUID deviceId) {
        this.timestamp = timestamp;
        this.measurementValue = measurementValue;
        this.deviceId = deviceId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public double getMeasurementValue() { return measurementValue; }
    public void setMeasurementValue(double measurementValue) { this.measurementValue = measurementValue; }
    public UUID getDeviceId() { return deviceId; }
    public void setDeviceId(UUID deviceId) { this.deviceId = deviceId; }
}