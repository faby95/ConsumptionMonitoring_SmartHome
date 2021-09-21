package com.smarthome.monitoring.message;

public class ManualMessage {

    private long timestamp;
    private String phoneName;
    private Boolean electricForniture;
    private Boolean gasForniture;
    private Boolean waterForniture;
    private double electricBound;
    private double gasBound;
    private double waterBound;

    public ManualMessage() {
        super();
    }

    public ManualMessage(long timestamp, String phoneName, Boolean electricForniture, Boolean gasForniture, Boolean waterForniture, double electricBound, double gasBound, double waterBound) {
        this.timestamp = timestamp;
        this.phoneName = phoneName;
        this.electricForniture = electricForniture;
        this.gasForniture = gasForniture;
        this.waterForniture = waterForniture;
        this.electricBound = electricBound;
        this.gasBound = gasBound;
        this.waterBound = waterBound;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public Boolean getElectricForniture() {
        return electricForniture;
    }

    public void setElectricForniture(Boolean electricForniture) {
        this.electricForniture = electricForniture;
    }

    public Boolean getGasForniture() {
        return gasForniture;
    }

    public void setGasForniture(Boolean gasForniture) {
        this.gasForniture = gasForniture;
    }

    public Boolean getWaterForniture() {
        return waterForniture;
    }

    public void setWaterForniture(Boolean waterForniture) {
        this.waterForniture = waterForniture;
    }

    public double getElectricBound() {
        return electricBound;
    }

    public void setElectricBound(double electricBound) {
        this.electricBound = electricBound;
    }

    public double getGasBound() {
        return gasBound;
    }

    public void setGasBound(double gasBound) {
        this.gasBound = gasBound;
    }

    public double getWaterBound() {
        return waterBound;
    }

    public void setWaterBound(double waterBound) {
        this.waterBound = waterBound;
    }

    @Override
    public String toString() {
        return "ManualMessage{" +
                "timestamp=" + timestamp +
                ", phoneName='" + phoneName + '\'' +
                ", electricForniture=" + electricForniture +
                ", gasForniture=" + gasForniture +
                ", waterForniture=" + waterForniture +
                ", electricBound=" + electricBound +
                ", gasBound=" + gasBound +
                ", waterBound=" + waterBound +
                '}';
    }
}
