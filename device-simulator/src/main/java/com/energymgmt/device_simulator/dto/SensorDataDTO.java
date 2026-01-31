package com.energymgmt.device_simulator.dto;

import java.io.Serializable;
import java.util.UUID;

public class SensorDataDTO implements Serializable {
    private long timestamp;
    private UUID deviceId;
    private double measurementValue;

    public SensorDataDTO(long timestamp, UUID deviceId, double measurementValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }

    public long getTimestamp() { return timestamp; }
    public UUID getDeviceId() { return deviceId; }
    public double getMeasurementValue() { return measurementValue; }
}