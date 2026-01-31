package com.energymgmt.device_service.dtos.builders;

import com.energymgmt.device_service.dtos.DeviceDTO;
import com.energymgmt.device_service.dtos.DeviceDetailsDTO;
import com.energymgmt.device_service.entities.Device;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        return new DeviceDTO(
                device.getId(),
                device.getName(),
                device.getMaxHourlyConsumption(),
                device.getUserId()
        );
    }

    public static Device toEntity(DeviceDetailsDTO dto) {
        Device device = new Device();
        device.setName(dto.getName());
        device.setDescription(dto.getDescription());
        device.setMaxHourlyConsumption(dto.getMaxHourlyConsumption());
        device.setUserId(dto.getUserId());
        return device;
    }
    public static DeviceDetailsDTO toDeviceDetailsDTO(Device device) {
        return new DeviceDetailsDTO(
                device.getId(),
                device.getName(),
                device.getMaxHourlyConsumption(),
                device.getDescription(),
                device.getUserId()
        );
    }
}