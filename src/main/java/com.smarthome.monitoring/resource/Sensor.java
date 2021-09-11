package com.smarthome.monitoring.resource;
import java.util.UUID;

public abstract class Sensor<T> {

    //Variabili della classe
    private String id;          //id del dispositivo univoco
    private String deviceName;  //nome dispositivo
    protected T value = null;   //valore di consumo misurato (incrementale) del dispositivo

    //Costruttori della classe
    public Sensor() {
        this.id = UUID.randomUUID().toString();
        this.deviceName = null;
        //Inizializzare "value" nella sottoclasse tramite funzione initializeValue
    }

    public Sensor(String id, String deviceName) {
        this.id = id;
        this.deviceName = deviceName;
        //Inizializzare "value" nella sottoclasse tramite funzione initializeValue
    }

    //Metodi della classe
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }


    //Gestione comportamentale del dispositivo sulla variabile value
    //..............................................................

    public abstract void initializeValue(T value);  //Possibilità di inizializzazione personalizzata per una sottoclasse

    public T updatedValue() {
        return this.value;
    }

}
