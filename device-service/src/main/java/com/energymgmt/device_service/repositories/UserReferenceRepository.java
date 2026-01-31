package com.energymgmt.device_service.repositories;

import com.energymgmt.device_service.entities.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserReferenceRepository extends JpaRepository<UserReference, UUID> {
}