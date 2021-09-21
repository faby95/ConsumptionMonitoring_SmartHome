package com.smarthome.monitoring.sub;
import java.util.ArrayList;
import java.util.UUID;
import com.smarthome.monitoring.message.Messages;
import com.smarthome.monitoring.message.InfoMessage;
import com.smarthome.monitoring.message.StatesMessage;
import com.smarthome.monitoring.message.ValueMessage;
import com.smarthome.monitoring.message.ManualMessage;
import com.smarthome.monitoring.pub.SmartHome;
import com.smarthome.monitoring.pub.SmartPhone;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PolicyManager {

    //eventualmente nella classe SmartHome è possibile pubblicare il valore del singolo sensore sfruttando l'id sensore nel topic
    //in questo caso evito perchè mi interessa aggregare i consumi a mo di contatore generale per capire se supero i limiti di fornitura per la smart home imposti
    //policy manager agisce in locale per agire tempestivamente anche in assenza di connessione
    //utilizzo della funzionalità bridge del broker eventualmente per spostare i dati in cloud per verificare ed analizzare i consumi (dei singoli dispositivi o valori aggregati di fornitura)
    private final static Logger logger = LoggerFactory.getLogger(PolicyManager.class);  //logger

    private double electricBoundedValue;
    private double gasBoundedValue;
    private double waterBoundedValue;
    private Boolean electricForniture;
    private Boolean gasForniture;
    private Boolean waterForniture;
    protected ArrayList<InfoMessage> electricInfoMessages = new ArrayList<InfoMessage>();
    protected ArrayList<InfoMessage> gasInfoMessages = new ArrayList<InfoMessage>();
    protected ArrayList<InfoMessage> waterInfoMessages = new ArrayList<InfoMessage>();
    private static final String electricTypeDate = "Electricity-Monitoring";   //Per gli InfoMessage
    private static final String gasTypeDate = "Gas-Monitoring";
    private static final String waterTypeDate = "Water-Monitoring";


    //Variabili broker
    private final static String BROKER_IP = "127.0.0.1";
    private final static int BROKER_PORT = 1883;
    //User e password da inserire ed usare nelle option se messe (per accesso al broker)
    //MQTT_USER
    //MQTT_PASSWORD
    //MQTT clients
    //Mqtt clients
    private IMqttClient subInfoResources;
    private IMqttClient subValueResources;
    private IMqttClient subExternalCommands;
    private IMqttClient pubStatusRecources;

    //Topic
    //Prende le informazioni dalla casa che gestisce, variabili statiche

    //Costruttore
    public PolicyManager(double electricBound, double gasBound, double waterBound) {
        this.electricBoundedValue = electricBound;
        this.gasBoundedValue = gasBound;
        this.waterBoundedValue = waterBound;
        this.electricForniture = true;       //Fornitura di default attiva
        this.gasForniture = true;
        this.waterForniture = true;
        this.subInfoResources = connectToBroker(String.format("policymanager-%s-info",SmartHome.smartHome),true); //retain dalla smarthome
        this.subValueResources = connectToBroker(String.format("policymanager-%s-values",SmartHome.smartHome),true);  //resta in ascolto sulla ricezione dei valori freschi e gestisci la fornitura
        this.subExternalCommands = connectToBroker(String.format("policymanager-%s-external",SmartHome.smartHome),true);  //gestione dei comandi esterni da smartphone
        this.pubStatusRecources = connectToBroker(String.format("policymanager-%s-status",SmartHome.smartHome),true);  //pub retain
        getInfoSmartObjects();
        getValueFornitures();
        getExternalCommands();
    }


    //Metodi
    //Metodo info, popola gli array con rappresentazione dei dispositivi smart presenti nella casa (retain)
    private void getInfoSmartObjects(){
        try{
            subInfoResources.subscribe(String.format("%s/#",SmartHome.topicInfo), 2, (s, mqttMessage) -> {
                byte[] payload = mqttMessage.getPayload();
                InfoMessage msgInfoResources = Messages.parseInfoMessage(payload);
                if(msgInfoResources != null){
                    switch (msgInfoResources.getSensorType()) {
                        case electricTypeDate:
                            electricInfoMessages.add(msgInfoResources);
                            break;
                        case gasTypeDate:
                            gasInfoMessages.add(msgInfoResources);
                            break;
                        case waterTypeDate:
                            waterInfoMessages.add(msgInfoResources);
                            break;
                    }
                    //Stampa il popolamento della lista step by step
                    logger.info("Electric list {}",electricInfoMessages);
                    logger.info("Gas list {}",gasInfoMessages);
                    logger.info("Water list {}",waterInfoMessages);
                }
                else{
                    logger.info("Message Received ({}) Message Received: {}", s, new String(payload));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //Metodo che prende decisioni sull'erogazione della fornitura sulla base dei valori freschi in arrivo dai sensori
    private void getValueFornitures(){
        try{
            //Si sottoscrive a tutti i valori di fornitura consumati
            subValueResources.subscribe(String.format("/%s/%s/+/value",SmartHome.monitoringType,SmartHome.smartHome), 1, (s, mqttMessage) -> {
                byte[] payload = mqttMessage.getPayload();
                ValueMessage msgValueResources = Messages.parseValueMessage(payload);
                if(msgValueResources != null){
                    setForniture(msgValueResources);  //Controllo il consumo ed eventualmente blocco la fornitura
                    StatesMessage newStates = new StatesMessage(System.currentTimeMillis(),
                            SmartHome.smartHome,
                            electricForniture,
                            waterForniture,
                            gasForniture);

                    logger.info("Message arrived from ({}) -> Publish new retain states",s);
                    publishData(pubStatusRecources,SmartHome.topicState,Messages.buildStatesJsonMessage(newStates),1,true);
                    logger.info("New retain states published E:{} G:{} W:{}",newStates.getElectricForniture(),newStates.getGasForniture(),newStates.getWaterForniture());
                }
                else{
                    logger.info("Message Received ({}) Message Received: {}", s, new String(payload));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //Metodo che gestisce i comandi esterni in arrivo dallo smartphone
    private void getExternalCommands(){
        try{
            //Si sottoscrive per ricevere i comandi esterni
            subExternalCommands.subscribe(SmartPhone.topicManual, 2, (s, mqttMessage) -> {
                byte[] payload = mqttMessage.getPayload();
                ManualMessage msg = Messages.parseManualMessage(payload);
                if(msg != null){
                    if(msg.getPhoneName().equals(SmartPhone.smartphoneName)){
                        logger.info("Change setting request received from {} granted",msg.getPhoneName());
                        changeElectricBound(msg.getElectricBound());
                        changeGasBound(msg.getGasBound());
                        changeWaterBound(msg.getWaterBound());
                        changeElectricForniture(msg.getElectricForniture());
                        changeGasForniture(msg.getGasForniture());
                        changeWaterForniture(msg.getWaterForniture());
                        logger.debug("All new settings changed");
                    }
                }
                else{
                    logger.info("Message Received ({}) Message Received: {}", s, new String(payload));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //Metodi per comandi esterni
    private void changeElectricBound(double e){
        this.electricBoundedValue = e;
        logger.info("Changed electric bound value to {}",this.electricBoundedValue);
    }

    private void changeGasBound(double g){
        this.gasBoundedValue = g;
        logger.info("Changed gas bound value to {}",this.gasBoundedValue);
    }

    private void changeWaterBound(double w){
        this.waterBoundedValue = w;
        logger.info("Changed water bound value to {}",this.waterBoundedValue);
    }

    private void changeElectricForniture(Boolean e){
        this.electricForniture = e;
        logger.info("Changed electric forniture value to {}",this.electricForniture);
    }

    private void changeGasForniture(Boolean g){
        this.gasForniture = g;
        logger.info("Changed gas forniture value to {}",this.gasForniture);
    }

    private void changeWaterForniture(Boolean w){
        this.waterForniture = w;
        logger.info("Changed water forniture value to {}",this.waterForniture);
    }
    //______________________________________________

    //Metodo di controllo automatico
    private void setForniture(ValueMessage vm){
        switch (vm.getMonitoringType()) {
            case SmartHome.monitoringElectricType:
                if (vm.getValue() >= electricBoundedValue) {
                    electricForniture = false;
                }
                break;
            case SmartHome.monitoringGasType:
                if (vm.getValue() >= gasBoundedValue) {
                    gasForniture = false;
                }
                break;
            case SmartHome.monitoringWaterType:
                if (vm.getValue() >= waterBoundedValue) {
                    waterForniture = false;
                }
                break;
        }
    }


    //Metodi Mqtt
    private IMqttClient connectToBroker(String brokerId, Boolean cleanSession){
        try{
            //Indirizzo,clientId,persistenza
            String clientID;
            if(brokerId != null){
                clientID = brokerId;
            }
            else{
                clientID = UUID.randomUUID().toString();               //ID del client mqtt
            }
            MqttClientPersistence persistence = new MemoryPersistence();  //Persistenza temporanea in ram e non su disco

            IMqttClient client = new MqttClient(
                    String.format("tcp://%s:%d", BROKER_IP, BROKER_PORT),
                    clientID,
                    persistence);

            //Opzioni
            MqttConnectOptions options = new MqttConnectOptions();
            //options.setUserName(MQTT_USERNAME);
            //options.setPassword(new String(MQTT_PASSWORD).toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(cleanSession);
            options.setConnectionTimeout(10);

            //Connessione al broker
            client.connect(options);

            logger.info("Connected to the Broker, Client Id: {}", clientID);

            return client;
        }
        catch (MqttException e){
            e.printStackTrace();
            return null;
        }
    }

    private void publishData(IMqttClient mqttClient, String topic, String payload, int qos, Boolean retain) {
        try{
            logger.debug("Publishing to Topic: {} Payload: {}", topic, payload);

            if (mqttClient.isConnected() && payload != null && topic != null) {

                if(qos == 0 || qos == 1 || qos == 2 ){
                    MqttMessage msg = new MqttMessage(payload.getBytes());
                    msg.setQos(qos);
                    msg.setRetained(retain);
                    mqttClient.publish(topic,msg);
                    logger.debug("Data published");
                }
                else{
                    logger.error("Rejected qos value, it must to be (0 or 1 or 2)");
                }
            }
            else{
                logger.error("Client could not be connected or your playload is null or your topic can be null as well");
            }
        }
        catch(MqttException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        new PolicyManager(3000.00,7000.00,4000.00);
    }
}
