package com.energymgmt.device_service.controllers;

import com.energymgmt.device_service.dtos.DeviceDTO;
import com.energymgmt.device_service.dtos.DeviceDetailsDTO;
import com.energymgmt.device_service.dtos.UserAssignmentDTO;
import com.energymgmt.device_service.services.DeviceService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/devices")

public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CLIENT')")
    public ResponseEntity<List<DeviceDTO>> getDevices(Authentication authentication) {
        List<DeviceDTO> devices = deviceService.findAllDevicesForUser(authentication);
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRATOR', 'CLIENT')")
    public ResponseEntity<DeviceDetailsDTO> getDevice(@PathVariable("id") UUID id) {
        DeviceDetailsDTO dto = deviceService.findDeviceById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<UUID> createDevice(@Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        UUID deviceID = deviceService.insertDevice(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DeviceDetailsDTO> updateDevice(@PathVariable("id") UUID id,
                                                         @Valid @RequestBody DeviceDetailsDTO deviceDTO) {
        DeviceDetailsDTO updatedDTO = deviceService.updateDevice(id, deviceDTO);
        return new ResponseEntity<>(updatedDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") UUID id) {
        deviceService.deleteDevice(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{id}/assign-user")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<DeviceDTO> assignUser(@PathVariable("id") UUID deviceId,
                                                @Valid @RequestBody UserAssignmentDTO assignmentDTO) {

        DeviceDTO assignedDevice = deviceService.assignUserToDevice(deviceId, assignmentDTO.getUserId());
        return new ResponseEntity<>(assignedDevice, HttpStatus.OK);
    }
}