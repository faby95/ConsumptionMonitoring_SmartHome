package com.smarthome.monitoring.resource;

public interface Actuator<T> {

    public abstract void setActuatorState(T isOn);

    public abstract T getActuatorState();

    public abstract void changeActuatorState();

}
