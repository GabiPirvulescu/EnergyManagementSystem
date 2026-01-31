package com.energymgmt.monitoring_service.dtos;

public class HourlyConsumptionDTO {
    private long hour;
    private double consumption;

    public HourlyConsumptionDTO(long hour, double consumption) {
        this.hour = hour;
        this.consumption = consumption;
    }

    public long getHour() { return hour; }
    public void setHour(long hour) { this.hour = hour; }
    public double getConsumption() { return consumption; }
    public void setConsumption(double consumption) { this.consumption = consumption; }
}