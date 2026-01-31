package com.energymgmt.monitoring_service.repository;

import com.energymgmt.monitoring_service.entities.HourlyConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HourlyConsumptionRepository extends JpaRepository<HourlyConsumption, UUID> {

    Optional<HourlyConsumption> findByDeviceIdAndTimestamp(UUID deviceId, long timestamp);
    @Query("SELECT h FROM HourlyConsumption h WHERE h.deviceId = :deviceId AND h.timestamp >= :start AND h.timestamp < :end")
    List<HourlyConsumption> findByDeviceIdAndTimestampBetween(
            @Param("deviceId") UUID deviceId,
            @Param("start") long start,
            @Param("end") long end);
}