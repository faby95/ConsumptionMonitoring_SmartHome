package com.smarthome.monitoring.pub;
import com.smarthome.monitoring.message.Messages;
import com.smarthome.monitoring.message.ManualMessage;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

public class SmartPhone {
    //Semplice mqtt pub client per interagire manualmente col policy manager

    private final static Logger logger = LoggerFactory.getLogger(SmartPhone.class);  //logger
    public final static String topicManual = String.format("/%s/%s/smartphone",SmartHome.monitoringType,SmartHome.smartHome);
    public final static String smartphoneName = "Rossi-Smartphone";
    //Variabili broker
    private final static String BROKER_IP = "127.0.0.1";
    private final static int BROKER_PORT = 1883;
    //User e password da inserire ed usare nelle option se messe (per accesso al broker)
    //MQTT_USER
    //MQTT_PASSWORD
    //Mqtt clients
    private IMqttClient pubSmartphone;

    //Costruttore

    public SmartPhone(Boolean e, Boolean g, Boolean w, double ebound, double gbound, double wbound){
        this.pubSmartphone = connectToBroker("clientPub-rossi-smartphone",true);
        pubNewSettings(e,g,w,ebound,gbound,wbound);
    }

    //Metodi

    private void pubNewSettings(Boolean e, Boolean g, Boolean w, double eb, double gb, double wb){
        if(pubSmartphone != null){
            ManualMessage m = new ManualMessage(System.currentTimeMillis(),
                    SmartPhone.smartphoneName,
                    e,
                    g,
                    w,
                    eb,
                    gb,
                    wb
                    );
            publishData(pubSmartphone, SmartPhone.topicManual,Messages.buildManualJsonMessage(m),2,false);
            try{
                pubSmartphone.disconnect();
                pubSmartphone.close();
                logger.info("New settings sent by you to the policy manager = {}",m);
                logger.debug("Disconnected");
            }
            catch (MqttException ex){
                ex.printStackTrace();
            }
        }
        else{
            logger.error("Connection to broker not granted");
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
        new SmartPhone(true,true,true,3000.00,7000.00,4000.00);
    }

}
