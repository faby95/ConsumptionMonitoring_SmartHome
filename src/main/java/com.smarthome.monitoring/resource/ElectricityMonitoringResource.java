package com.smarthome.monitoring.resource;

//logger, delay, comportamento

public class ElectricityMonitoringResource extends Sensor<Double> implements Actuator<Boolean>{

    //logger nel device, serve un delay
    private Boolean isOn;     //Variabile di attuazione (fornitura on/off)
    private static final String sensorType = "Electricity-Monitoring";  //tipo di sensore, [acqua, luce, gas]
    private static final String measureType = "kWh - kilowatt-hour"; //unità di misura del sensore
    //variabili statiche final da passare al costruttore  type ecc kw/h

    public ElectricityMonitoringResource() {
        super();  //Costruttore della classe astratta Sensor senza parametri
        setActuatorState(true);
        initializeValue(0.0);
    }

    public ElectricityMonitoringResource(String id, String deviceName, Boolean isOn, Double value) {
        super(id, deviceName);  //Costruttore classe astratta Sensor
        setActuatorState(isOn);
        initializeValue(value);
    }

    //Metodi classe
    //_________________________

    public String getSensorType() {
        return ElectricityMonitoringResource.sensorType;
    }

    public String getMeasureType() {
        return ElectricityMonitoringResource.measureType;
    }

    //Metodi override Sensor
    //_______________________________________
    //_______________________________________

    @Override
    public void initializeValue(Double value) {
        this.value = value;
    }

    @Override  //provvisorio da definire
    public void sensorBehaviour() {
        if(this.isOn){
            this.value += 0.01;
        }else{
            this.value = 0.0;
        }
    }

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
