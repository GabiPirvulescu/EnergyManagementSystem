package com.energymgmt.device_service.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class UserAssignmentDTO {

    @NotNull(message = "User ID is required")
    private UUID userId;

    public UserAssignmentDTO() {
    }

    public UserAssignmentDTO(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
}