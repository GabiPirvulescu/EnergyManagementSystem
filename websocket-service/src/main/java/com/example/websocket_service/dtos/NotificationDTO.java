package com.example.websocket_service.dtos;

import java.util.UUID;

public class NotificationDTO {
    private String message;
    private UUID userId;
    private UUID deviceId;
    private long timestamp;

    public NotificationDTO() {
    }

    public NotificationDTO(String message, UUID userId, UUID deviceId, long timestamp) {
        this.message = message;
        this.userId = userId;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}