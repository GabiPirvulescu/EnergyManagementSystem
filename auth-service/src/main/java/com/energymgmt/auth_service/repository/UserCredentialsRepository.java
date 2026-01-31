package com.energymgmt.auth_service.repository;

import com.energymgmt.auth_service.entities.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, UUID> {

    /**
     * Finds a user by username.
     * Use Optional to handle the cases when user is null.
     */
    Optional<UserCredentials> findByUsername(String username);
}