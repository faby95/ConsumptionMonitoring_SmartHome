package com.smarthome.monitoring.resource;

public abstract class GasMonitoringResource extends Sensor<Double> implements Actuator<Boolean>{

    private Boolean isOn;     //Variabile di attuazione (fornitura on/off)
    private static final String sensorType = "Gas-Monitoring";  //tipo di sensore
    private static final String measureType = "kg"; //unità di misura del sensore

    public GasMonitoringResource() {
        super();  //Costruttore della classe astratta Sensor senza parametri
        setActuatorState(true);
        initializeValue(0.0);
    }

    public GasMonitoringResource(String id, String deviceName, Boolean isOn) {
        super(id, deviceName);
        setActuatorState(isOn);
        initializeValue(0.0);
    }

    //Metodi classe
    //_________________________

    public String getSensorType() {
        return GasMonitoringResource.sensorType;
    }

    public String getMeasureType() {
        return GasMonitoringResource.measureType;
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
