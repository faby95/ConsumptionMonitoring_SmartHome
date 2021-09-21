package com.smarthome.monitoring.pub;
import com.smarthome.monitoring.message.InfoMessage;
import com.smarthome.monitoring.message.Messages;
import com.smarthome.monitoring.message.StatesMessage;
import com.smarthome.monitoring.message.ValueMessage;
import com.smarthome.monitoring.resource.ElectricDevice;
import com.smarthome.monitoring.resource.GasDevice;
import com.smarthome.monitoring.resource.WaterDevice;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.UUID;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartHome {

    //Variabili classe
    public final static String monitoringGasType = "Gas_Monitoring_Resources";
    public final static String monitoringWaterType = "Water_Monitoring_Resources";
    public final static String monitoringElectricType = "Electric_Monitoring_Resources";
    public final static String monitoringType = "consumption"; //base topic
    public final static String smartHome = "rossihouse0001";  //house topic
    public final static String topicInfo = String.format("/%s/%s/info",SmartHome.monitoringType,SmartHome.smartHome); //SmartObjects della casa Rossi di tipo consumption monitoring //Retain  pub (unico messaggio lista da modellare con array di oggetti qos2) setup
    public final static String topicGasValue = String.format("/%s/%s/gas/value",SmartHome.monitoringType,SmartHome.smartHome); //Retain?  pub
    public final static String topicElectricValue = String.format("/%s/%s/electric/value",SmartHome.monitoringType,SmartHome.smartHome); //Retain?  pub
    public final static String topicWaterValue = String.format("/%s/%s/water/value",SmartHome.monitoringType,SmartHome.smartHome); //Retain?  pub
    public final static String topicState = String.format("/%s/%s/states",SmartHome.monitoringType,SmartHome.smartHome);  //Se lo prende dal sub e ci si sottoscrive nel thread principale
    protected ArrayList<ElectricDevice> electricDevicesList = new ArrayList<ElectricDevice>();
    protected ArrayList<WaterDevice> waterDevicesList = new ArrayList<WaterDevice>();
    protected ArrayList<GasDevice> gasDevicesList = new ArrayList<GasDevice>();
    private final static Logger logger = LoggerFactory.getLogger(SmartHome.class);  //logger
    //Variabili broker
    private final static String BROKER_IP = "127.0.0.1";
    private final static int BROKER_PORT = 1883;
    //User e password da inserire ed usare nelle option se messe (per accesso al broker)
    //MQTT_USER
    //MQTT_PASSWORD
    //Mqtt clients
    private IMqttClient pubElectricResources;
    private IMqttClient pubGasResources;
    private IMqttClient pubWaterResources;
    private IMqttClient subStatusRecources;
    //Timer per la gestione dei task
    Timer managerGasResources;
    Timer managerWaterResources;
    Timer managerElectricResources;


    //Costruttore
    public SmartHome(){
        //Inizializzare i Timer
        this.managerGasResources = new Timer();
        this.managerWaterResources = new Timer();
        this.managerElectricResources = new Timer();
        //Client persistenti per tutta la durata del programma
        this.pubElectricResources = connectToBroker("electric0001-rossi-house",true);
        this.pubGasResources = connectToBroker("gas0001-rossi-house",true);
        this.pubWaterResources = connectToBroker("water0001-rossi-house", true);
        this.subStatusRecources = connectToBroker("forniture0001-rossi-house",true);  //retain
        //Inserire oggetti nelle liste
        createSmartObjects();       //Creazione oggetti
        manageInfoResources();      //Gestione messaggi info (pub)
        manageStatesResources();    //Gestione stati forniture (sub)
        manageGasResources();       //Gestione risorse gas (pub)
        manageElectricResources();  //Gestione risorse elettriche (pub)
        manageWaterResources();     //Gestione risorse acqua (pub)
    }

    //Metodi
    //Metodo per il costruttore
    private void createSmartObjects(){
        //Dispositivi elettrici
        //____________________________________________________________________________________KITCHEN
        this.electricDevicesList.add(new ElectricDevice("e0001","TV","Kitchen",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0002","Lighting","Kitchen",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0003","Fridge","Kitchen",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0004","Oven","Kitchen",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0005","Dishwasher","Kitchen",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0006","Microwave-oven","Kitchen",true,false));
        //_____________________________________________________________________________________BATHROOM
        this.electricDevicesList.add(new ElectricDevice("e0007","Washing-machine","Bathroom",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0008","Lighting","Bathroom",true,false));
        //_____________________________________________________________________________________LIVINGROOM
        this.electricDevicesList.add(new ElectricDevice("e0009","Lighting","Livingroom",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0010","TV","Livingroom",true,false));
        //_____________________________________________________________________________________BEDROOM
        this.electricDevicesList.add(new ElectricDevice("e0011","Lighting","Bedroom",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0012","PC","Bedroom",true,false));
        this.electricDevicesList.add(new ElectricDevice("e0013","TV","Bedroom",true,false));

        //Dispositivi gas
        //____________________________________________________________________________________KITCHEN
        this.gasDevicesList.add(new GasDevice("g0001","Radiator","Kitchen",true,false));
        this.gasDevicesList.add(new GasDevice("g0002","Cooker","Kitchen",true,false));
        //_____________________________________________________________________________________BATHROOM
        this.gasDevicesList.add(new GasDevice("g0003","Boiler","Bathroom",true,false));
        this.gasDevicesList.add(new GasDevice("g0004","Radiator","Bathroom",true,false));
        //_____________________________________________________________________________________LIVINGROOM
        this.gasDevicesList.add(new GasDevice("g0005","Radiator","Livingroom",true,false));
        //_____________________________________________________________________________________BEDROOM
        this.gasDevicesList.add(new GasDevice("g0006","Radiator","Bedroom",true,false));

        //Dispositivi acqua
        //____________________________________________________________________________________KITCHEN
        this.waterDevicesList.add(new WaterDevice("w0001","Sink","Kitchen",true,false));
        //_____________________________________________________________________________________BATHROOM
        this.waterDevicesList.add(new WaterDevice("w0002","Sink","Bathroom",true,false));
        this.waterDevicesList.add(new WaterDevice("w0003","Shower","Bathroom",true,false));
        //_____________________________________________________________________________________LIVINGROOM
        //_____________________________________________________________________________________BEDROOM
        //_____________________________________________________________________________________GARDEN
        this.waterDevicesList.add(new WaterDevice("w0004","Sprinkler","Garden",true,false));

    }

    //Manage info
    private void manageInfoResources(){
        IMqttClient pubInfoResources = connectToBroker("info0001-rossi-house",true);

        if(pubInfoResources != null){
            for(ElectricDevice device : this.electricDevicesList){
                InfoMessage m = new InfoMessage(System.currentTimeMillis(),
                        device.getId(),
                        device.getDeviceName(),
                        device.getSensorType(),
                        SmartHome.smartHome,
                        device.getLocation()
                );
                publishData(pubInfoResources,String.format("%s/electric/%s",SmartHome.topicInfo,device.getId()), Messages.buildInfoJsonMessage(m),2,true);
            }
            for(WaterDevice device : this.waterDevicesList){
                InfoMessage m = new InfoMessage(System.currentTimeMillis(),
                        device.getId(),
                        device.getDeviceName(),
                        device.getSensorType(),
                        SmartHome.smartHome,
                        device.getLocation()
                );
                publishData(pubInfoResources,String.format("%s/water/%s",SmartHome.topicInfo,device.getId()), Messages.buildInfoJsonMessage(m),2,true);
            }
            for(GasDevice device : this.gasDevicesList){
                InfoMessage m = new InfoMessage(System.currentTimeMillis(),
                        device.getId(),
                        device.getDeviceName(),
                        device.getSensorType(),
                        SmartHome.smartHome,
                        device.getLocation()
                );
                publishData(pubInfoResources,String.format("%s/gas/%s",SmartHome.topicInfo,device.getId()), Messages.buildInfoJsonMessage(m),2,true);
            }
            try{
                pubInfoResources.disconnect();
                pubInfoResources.close();
            }
            catch (MqttException e){
                e.printStackTrace();
            }
        }
        else{
            logger.error("Connection to broker not granted");
        }
    }

    //Manage resources
    private void manageGasResources(){

        this.managerGasResources.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                double v = 0;  //Valore aggregato della fornitura consumata
                String mt = null;  //Unità di misura
                for(GasDevice device : gasDevicesList){
                    v = v + device.updatedValue();
                    if(mt == null){
                        mt = device.getMeasureType();
                    }
                }
                ValueMessage m = new ValueMessage(System.currentTimeMillis(),
                        SmartHome.smartHome,
                        SmartHome.monitoringGasType,
                        mt,
                        v);

                publishData(pubGasResources,SmartHome.topicGasValue,Messages.buildValueJsonMessage(m),1,false);
                //Clean session false to the policy manager
            }
        },1000,5000); //Pubblica i valori della fornitura usata di gas ogni 5 secondi

    }

    private void manageWaterResources(){
        this.managerWaterResources.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                double v = 0;  //Valore aggregato della fornitura consumata
                String mt = null;  //Unità di misura
                for(WaterDevice device : waterDevicesList){
                    v = v + device.updatedValue();
                    if(mt == null){
                        mt = device.getMeasureType();
                    }
                }
                ValueMessage m = new ValueMessage(System.currentTimeMillis(),
                        SmartHome.smartHome,
                        SmartHome.monitoringWaterType,
                        mt,
                        v);

                publishData(pubWaterResources,SmartHome.topicWaterValue,Messages.buildValueJsonMessage(m),1,false);
                //Clean session false to the policy manager
            }
        },1000,5000); //Pubblica i valori della fornitura usata di acqua ogni 5 secondi
    }

    private void manageElectricResources(){
        this.managerElectricResources.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                double v = 0;  //Valore aggregato della fornitura consumata
                String mt = null;  //Unità di misura
                for(ElectricDevice device : electricDevicesList){
                    v = v + device.updatedValue();
                    if(mt == null){
                        mt = device.getMeasureType();
                    }
                }
                ValueMessage m = new ValueMessage(System.currentTimeMillis(),
                        SmartHome.smartHome,
                        SmartHome.monitoringElectricType,
                        mt,
                        v);

                publishData(pubElectricResources,SmartHome.topicElectricValue,Messages.buildValueJsonMessage(m),1,false);
                //Clean session false to the policy manager
            }
        },1000,5000); //Pubblica i valori della fornitura usata di elettricità ogni 5 secondi
    }

    //Manage states
    private void manageStatesResources(){
        try{
            //Mi metto in ascolto dei messaggi di cambio stato, alla ricezione eseguo la transizione
            //new IMqttMessageListener(s=topic, mqttMessage), rimpiazzo con lambda
            subStatusRecources.subscribe(SmartHome.topicState, 1, (s, mqttMessage) -> {
                byte[] payload = mqttMessage.getPayload();
                StatesMessage msgFornitureStates = Messages.parseStatesMessage(payload);
                if(msgFornitureStates != null){
                    logger.info("States message received = Timestamp: {} Home: {}  F1: {} F2: {} F3: {}",
                            msgFornitureStates.getTimestamp(),msgFornitureStates.getSmartHome(),
                            msgFornitureStates.getElectricForniture(),msgFornitureStates.getWaterForniture(),
                            msgFornitureStates.getGasForniture());
                    //Imposto gli attuatori
                    setFornitureStates(msgFornitureStates.getElectricForniture(),
                            msgFornitureStates.getGasForniture(),
                            msgFornitureStates.getWaterForniture());
                    logger.info("Set new forniture actuator value received");
                }
                else{
                    logger.info("Message Received ({}) Message Received: {}", s, new String(payload));
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }


    private void setFornitureStates(Boolean electric, Boolean gas, Boolean water){
        for(ElectricDevice device : electricDevicesList){
            device.setActuatorState(electric);
        }
        for(GasDevice device : gasDevicesList){
            device.setActuatorState(gas);
        }
        for(WaterDevice device : waterDevicesList){
            device.setActuatorState(water);
        }
    }



    //MQTT METODI

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
        new SmartHome();
    }
}
