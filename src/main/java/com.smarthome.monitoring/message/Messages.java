package com.smarthome.monitoring.message;
import com.google.gson.Gson;

public class Messages {

    //Costruttore
    public Messages() {
        super();
    }

    //Metodi statici messaggi
    public static ManualMessage parseManualMessage(byte[] payload){
        try{
            Gson gson = new Gson();
            return (ManualMessage) gson.fromJson(new String(payload), ManualMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static StatesMessage parseStatesMessage(byte[] payload) {
        try {
            Gson gson = new Gson();
            return (StatesMessage) gson.fromJson(new String(payload), StatesMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InfoMessage parseInfoMessage(byte[] payload) {
        try {
            Gson gson = new Gson();
            return (InfoMessage) gson.fromJson(new String(payload), InfoMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ValueMessage parseValueMessage(byte[] payload) {
        try {
            Gson gson = new Gson();
            return (ValueMessage) gson.fromJson(new String(payload), ValueMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //____________________________________________________________________________________________________

    public static String buildInfoJsonMessage(InfoMessage m) {

        try {
            Gson gson = new Gson();
            return gson.toJson(m);

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String buildValueJsonMessage(ValueMessage m) {

        try {
            Gson gson = new Gson();
            return gson.toJson(m);

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String buildStatesJsonMessage(StatesMessage m) {

        try {
            Gson gson = new Gson();
            return gson.toJson(m);

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String buildManualJsonMessage(ManualMessage m) {

        try {
            Gson gson = new Gson();
            return gson.toJson(m);

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
