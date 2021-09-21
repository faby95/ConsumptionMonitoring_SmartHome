package com.smarthome.monitoring.message;

public class StatesMessage {

    private long timestamp;
    private String smartHome;
    private Boolean electricForniture;
    private Boolean waterForniture;
    private Boolean gasForniture;

    public StatesMessage() {
        super();
    }

    public StatesMessage(long timestamp, String smartHome, Boolean electricForniture, Boolean waterForniture, Boolean gasForniture) {
        this.timestamp = timestamp;
        this.smartHome = smartHome;
        this.electricForniture = electricForniture;
        this.waterForniture = waterForniture;
        this.gasForniture = gasForniture;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSmartHome() {
        return smartHome;
    }

    public void setSmartHome(String smartHome) {
        this.smartHome = smartHome;
    }

    public Boolean getElectricForniture() {
        return electricForniture;
    }

    public void setElectricForniture(Boolean electricForniture) {
        this.electricForniture = electricForniture;
    }

    public Boolean getWaterForniture() {
        return waterForniture;
    }

    public void setWaterForniture(Boolean waterForniture) {
        this.waterForniture = waterForniture;
    }

    public Boolean getGasForniture() {
        return gasForniture;
    }

    public void setGasForniture(Boolean gasForniture) {
        this.gasForniture = gasForniture;
    }

    @Override
    public String toString() {
        return "StatesMessage{" +
                "timestamp=" + timestamp +
                ", smartHome='" + smartHome + '\'' +
                ", electricForniture=" + electricForniture +
                ", waterForniture=" + waterForniture +
                ", gasForniture=" + gasForniture +
                '}';
    }
}
