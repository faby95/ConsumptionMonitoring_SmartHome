package com.smarthome.monitoring.message;

public class InfoMessage {

    private long timestamp;
    private String sensorId;
    private String deviceName;
    private String sensorType;
    private String smartHome; //Casa di appartenenza
    private String location;  //Stanza del sensore

    public InfoMessage(){
        super();
    }

    public InfoMessage(long timestamp, String sensorId, String deviceName, String sensorType, String smartHome, String location) {
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.deviceName = deviceName;
        this.sensorType = sensorType;
        this.smartHome = smartHome;
        this.location = location;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSmartHome() {
        return smartHome;
    }

    public void setSmartHome(String smartHome) {
        this.smartHome = smartHome;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "InfoMessage{" +
                "timestamp=" + timestamp +
                ", sensorId='" + sensorId + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", sensorType='" + sensorType + '\'' +
                ", smartHome='" + smartHome + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
