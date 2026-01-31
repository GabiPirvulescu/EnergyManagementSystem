package com.energymgmt.monitoring_service.repository;

import com.energymgmt.monitoring_service.entities.MonitoredDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MonitoredDeviceRepository extends JpaRepository<MonitoredDevice, UUID> {
}