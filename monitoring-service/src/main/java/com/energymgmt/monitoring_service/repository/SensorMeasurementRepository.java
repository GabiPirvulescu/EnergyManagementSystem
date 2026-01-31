package com.energymgmt.monitoring_service.repository;

import com.energymgmt.monitoring_service.entities.SensorMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SensorMeasurementRepository extends JpaRepository<SensorMeasurement, UUID> {
}