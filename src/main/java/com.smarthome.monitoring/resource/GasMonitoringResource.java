package com.smarthome.monitoring.resource;

public class GasMonitoringResource extends Sensor<Double> implements Actuator<Boolean>{

    //logger nel device, serve un delay
    private Boolean isOn;     //Variabile di attuazione (fornitura on/off)
    private static final String sensorType = "Gas-Monitoring";  //tipo di sensore, [acqua, luce, gas]
    private static final String measureType = "kg"; //unità di misura del sensore
    //variabili statiche final da passare al costruttore  type ecc kw/h

    public GasMonitoringResource() {
        super(); //posso fare anche personalizzato
        setActuatorState(true);
        initializeValue(0.0);
    }

    public GasMonitoringResource(String id, String deviceName, Boolean isOn, Double value) {
        super(id, deviceName);
        setActuatorState(isOn);
        initializeValue(value);
    }


    //Metodi classe

    public String getSensorType() {
        return GasMonitoringResource.sensorType;
    }

    public String getMeasureType() {
        return GasMonitoringResource.measureType;
    }

    //Override Sensor metodi

    @Override
    public void initializeValue(Double value) {
        this.value = value;
    }

    @Override  //Provvisorio da definire
    public void sensorBehaviour() {
        if(this.isOn){
            this.value += 0.01;
        }else{
            this.value = 0.0;
        }
    }

    //Override attuatore metodi

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
