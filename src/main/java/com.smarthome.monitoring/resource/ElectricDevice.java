package com.smarthome.monitoring.resource;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Math;

public class ElectricDevice extends ElectricityMonitoringResource{

    //Variabili della classe
    private Boolean switchButton;  //Variabile comportamentale di simulazione
    private String location;

    //Costruttori
    public ElectricDevice(String deviceName, String location ,Boolean switchButton ) {
        super(); //Costruttore della classe astratta ElectricityMonitoringResource senza parametri
        this.location = location;
        this.switchButton = switchButton;
        setDeviceName(deviceName);
        use();
    }

    public ElectricDevice(String id, String deviceName, String location, Boolean isOn, Boolean switchButton) {
        super(id, deviceName, isOn);
        this.location = location;
        this.switchButton = switchButton;
        use();
    }

    //Metodi
    public Boolean getSwitchButton() {
        return this.switchButton;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setSwitchButton(Boolean s) { this.switchButton = s; }


    @Override
    public void sensorBehaviour() {
        //Se la fornitura è accesa e lo strumento è utilizzato...
        if(getActuatorState() && this.switchButton){
            setValue(updatedValue() + generateBoundedDouble());
        }
        else{
            if(!(getActuatorState())){
                setValue(0.0);  //Reset del valore se la fornitura è stata spenta
            }
        }
    }

    //use da utilizzare nel costruttore
    private void use(){
        //Creo gli oggetti timer tBehaviour e tSwitch
        Timer tBehaviour = new Timer();
        Timer tSwitch = new Timer();

        //Definisco il task del cambio stato dello switch del device
        tSwitch.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setSwitchButton(!(getSwitchButton()));
            }
        },5000,4000);

        //Definisco il task del comportamento
        tBehaviour.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sensorBehaviour();
            }
        },2500,1000);
    }

    //Funzione statica per generare un valore randomico double entro i limiti imposti (usata in sensorBehaviour)
    private static double generateBoundedDouble() {
        double leftLimit = 1.0;
        double rightLimit = 10.0;
        return leftLimit + (Math.random() * (rightLimit - leftLimit));
    }
}
