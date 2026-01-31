package com.energymgmt.auth_service.builders;

import com.energymgmt.auth_service.dtos.UserCredentialsDTO;
import com.energymgmt.auth_service.entities.UserCredentials;

public class UserCredentialsBuilder {

    private UserCredentialsBuilder() {
    }

    public static UserCredentialsDTO toUserCredentialsDTO(UserCredentials user) {
        return new UserCredentialsDTO(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}