package com.smarthome.monitoring.message;

public class ValueMessage {

    private long timestamp;
    private String smartHome;
    private String monitoringType; //Water, Gas, Electric
    private String measureType;    //L, kg, kw/h
    private double value;  //Valore aggregato del consumo dei sensori data una tipologia (singolo contatore per fornitura)

    public ValueMessage() {
        super();
    }

    public ValueMessage(long timestamp, String smartHome, String monitoringType, String measureType, double value) {
        this.timestamp = timestamp;
        this.smartHome = smartHome;
        this.monitoringType = monitoringType;
        this.measureType = measureType;
        this.value = value;
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

    public String getMonitoringType() {
        return monitoringType;
    }

    public void setMonitoringType(String monitoringType) {
        this.monitoringType = monitoringType;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ValueMessage{" +
                "timestamp=" + timestamp +
                ", smartHome='" + smartHome + '\'' +
                ", monitoringType='" + monitoringType + '\'' +
                ", measureType='" + measureType + '\'' +
                ", value=" + value +
                '}';
    }
}
