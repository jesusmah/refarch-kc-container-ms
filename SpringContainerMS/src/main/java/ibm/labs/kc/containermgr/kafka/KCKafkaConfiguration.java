package ibm.labs.kc.containermgr.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KCKafkaConfiguration {
	
    public static final String ORDERS_TOPIC = "orders";
    public static final String REJECTED_ORDERS_TOPIC = "rejected-orders";
    public static final String ALLOCATED_ORDERS_TOPIC = "allocated-orders";
    public static final String CONTAINERS_TOPIC = "containers";
    public static final String ICP_ENV = "ICP";
    public static final String IC_ENV = "IBMCLOUD";
    
    public static Map<String, Object> getConsumerProperties(String groupID) {
    	Map<String, Object>  props = buildCommonProperties();
    	props.put(ConsumerConfig.GROUP_ID_CONFIG, groupID);
 	    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
 	    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
 	    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
 	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    	return props;
    }
    
    
    private static Map<String, Object> buildCommonProperties() {
    	Map<String, Object> properties = new HashMap<String,Object>();
        Map<String, String> env = System.getenv();

        if (IC_ENV.equals(env.get("KAFKA_ENV")) || ICP_ENV.equals(env.get("KAFKA_ENV"))) {
            if (env.get("KAFKA_BROKERS") == null) {
                throw new IllegalStateException("Missing environment variable KAFKA_BROKERS");
            }
            if (env.get("KAFKA_APIKEY") == null) {
                throw new IllegalStateException("Missing environment variable KAFKA_APIKEY");
            }
            properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, env.get("KAFKA_BROKERS"));
            properties.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            properties.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
            properties.put(SaslConfigs.SASL_JAAS_CONFIG,
                    "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\""
                            + env.get("KAFKA_APIKEY") + "\";");
            properties.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
            if (env.get("JKS_LOCATION") != null) {
            	 properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, env.get("JKS_LOCATION"));
            }
            if (env.get("TRUSTSTORE_PWD") != null) {
            	properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, env.get("TRUSTSTORE_PWD"));
            }
            
            properties.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
            properties.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        } else {
            if (env.get("KAFKA_BROKERS") == null) {
                properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            } else {
                properties.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, env.get("KAFKA_BROKERS"));
            }
        }

        return properties;
    }

}