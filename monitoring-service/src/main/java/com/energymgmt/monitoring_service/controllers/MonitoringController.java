package com.energymgmt.monitoring_service.controllers;

import com.energymgmt.monitoring_service.dtos.HourlyConsumptionDTO;
import com.energymgmt.monitoring_service.entities.HourlyConsumption;
import com.energymgmt.monitoring_service.repository.HourlyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringController.class);
    private final HourlyConsumptionRepository hourlyConsumptionRepository;

    public MonitoringController(HourlyConsumptionRepository hourlyConsumptionRepository) {
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
    }

    @GetMapping("/consumption/{deviceId}")
    public ResponseEntity<List<HourlyConsumptionDTO>> getConsumption(
            @PathVariable UUID deviceId,
            @RequestParam long date) {

        LOGGER.info("Fetching consumption for device {} on date {}", deviceId, date);

        long startOfDay = date;
        long endOfDay = date + 86400000;

        List<HourlyConsumption> data = hourlyConsumptionRepository
                .findByDeviceIdAndTimestampBetween(deviceId, startOfDay, endOfDay);

        List<HourlyConsumptionDTO> dtos = data.stream()
                .map(hc -> new HourlyConsumptionDTO(hc.getTimestamp(), hc.getTotalConsumption()))
                .collect(Collectors.toList());

        LOGGER.info("Found {} hourly consumption records", dtos.size());

        return ResponseEntity.ok(dtos);
    }
}