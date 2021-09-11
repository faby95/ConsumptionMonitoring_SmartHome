package com.smarthome.monitoring.resource;

public abstract class WaterMonitoringResource extends Sensor<Double> implements Actuator<Boolean>{

    private Boolean isOn;     //Variabile di attuazione (fornitura on/off)
    private static final String sensorType = "Water-Monitoring";  //tipo di sensore
    private static final String measureType = "l/s - liter per second"; //unità di misura del sensore

    public WaterMonitoringResource() {
        super(); //posso personalizzare
        setActuatorState(true);
        initializeValue(0.0);
    }

    public WaterMonitoringResource(String id, String deviceName, Boolean isOn, Double value) {
        super(id, deviceName);
        setActuatorState(isOn);
        initializeValue(value);
    }

    //Metodi classe
    //_________________________

    public String getSensorType() {
        return WaterMonitoringResource.sensorType;
    }

    public String getMeasureType() {
        return WaterMonitoringResource.measureType;
    }

    //Metodo override Sensor e metodo astratto comportamentale
    //_______________________________________
    //_______________________________________

    @Override
    public void initializeValue(Double value) {
        this.value = value;
    }

    public abstract void sensorBehaviour();

    //Interfaccia attuatore
    //_______________________________________
    //_______________________________________

    @Override
    public void setActuatorState(Boolean isOn) {
        this.isOn = isOn;
    }

    @Override
    public Boolean getActuatorState() {
        return this.isOn;
    }

    @Override
    public void changeActuatorState() {
        this.isOn = !(this.isOn);
    }

}
