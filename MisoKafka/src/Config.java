
public class Config {
    public static final String TOPIC = "iot";
    public static final String TOPIC2 = "topic2";
    public static final String TOPIC3 = "topic3";
    
//    public static final String ZOOKEEPER_SERVER = "192.168.23.129:2181,zkserver2:2181";
    public static boolean DEBUG_MODE = false;	
    public static final String ZOOKEEPER_SERVER = "192.168.23.129:2181";
    public static final int ZOOKEEPER_SESSION_TIMEOUT_MS = 10000;
    public static final int ZOOKEEPER_CONNECTION_TIMEOUT_MS = 8000;
    
    public static final String KAFKA_SERVER = "192.168.23.129:9092";
    public static final int KAFKA_PARTITION_NUM = 1;
    public static final int KAFKA_REPLICATION_NUM = 1;
    public static final int KAFKA_CONSUMER_THREADS = 1;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int RETRIES = 3;
    public static final boolean SYNCHRONOUSLY = true; 
    public static final String PRODUCER_CLIENT_ID = "Miso_IOT_Producer";
    public static final String CONSUMER_CLIENT_ID = "Miso_IOT_Consumer_5";
    public static final String KAFKA_AUTO_OFFSET_RESET = "largest";	    // [smallest|largest]
    // smallest = receive message from begin use new groupID
    // largest = just receive new message use new groupID
  
    public static String ImageSavePath;
//    public static final String ImageSavePath = "E://XXX";
//    public static final String ImageSavePath = "/opt";
    
    private Config() {}
}
