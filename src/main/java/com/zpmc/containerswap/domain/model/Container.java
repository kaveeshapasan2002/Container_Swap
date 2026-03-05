package com.zpmc.containerswap.domain.model;

import java.time.LocalDateTime;

public class Container {

    private String containerId;
    private String type;
    private String status;
    private String yardLocation;
    private String vesselName;
    private double weightTons;
    private LocalDateTime lastAccessed;

    public Container() {}

    public Container(String containerId, String type, String status,
                     String yardLocation, String vesselName, double weightTons) {
        this.containerId = containerId;
        this.type = type;
        this.status = status;
        this.yardLocation = yardLocation;
        this.vesselName = vesselName;
        this.weightTons = weightTons;
        this.lastAccessed = LocalDateTime.now();
    }

    public void touch() {
        this.lastAccessed = LocalDateTime.now();
    }

    public String getContainerId() { return containerId; }
    public void setContainerId(String containerId) { this.containerId = containerId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getYardLocation() { return yardLocation; }
    public void setYardLocation(String yardLocation) { this.yardLocation = yardLocation; }

    public String getVesselName() { return vesselName; }
    public void setVesselName(String vesselName) { this.vesselName = vesselName; }

    public double getWeightTons() { return weightTons; }
    public void setWeightTons(double weightTons) { this.weightTons = weightTons; }

    public LocalDateTime getLastAccessed() { return lastAccessed; }
    public void setLastAccessed(LocalDateTime lastAccessed) { this.lastAccessed = lastAccessed; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | %.1ft",
                containerId, type, status, yardLocation, weightTons);
    }
}
