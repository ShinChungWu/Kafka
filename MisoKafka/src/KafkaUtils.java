import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

import java.util.Properties;
import java.util.logging.Logger;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;
import kafka.utils.ZkUtils;
import scala.Tuple2;
import scala.collection.Iterable;
import scala.collection.script.Start;


public class KafkaUtils {

	private static Logger _log = Logger.getLogger(Start.class.getName());

	static ZkClient zkClient = new ZkClient (
			Config.ZOOKEEPER_SERVER,
			Config.ZOOKEEPER_SESSION_TIMEOUT_MS,
			Config.ZOOKEEPER_CONNECTION_TIMEOUT_MS,
			ZKStringSerializer$.MODULE$
	);
	static boolean isSecureKafkaCluster = false;
	static ZkUtils zkUtils = new ZkUtils(zkClient, new ZkConnection(Config.ZOOKEEPER_SERVER), isSecureKafkaCluster);
	public static void createTopic(String topic) {
		// Security for Kafka was added in Kafka 0.9.0.0
		if(!AdminUtils.topicExists(zkUtils, Config.TOPIC)) {
			try {
				AdminUtils.createTopic(zkUtils, topic,Config.KAFKA_PARTITION_NUM, Config.KAFKA_REPLICATION_NUM, new Properties(), null);
			} catch (Exception e) {
				_log.info(e.getMessage());
			}
		}
	}
	
	public static void listTopic() {
		Iterable<Tuple2<String,Properties>> topics = AdminUtils.fetchAllTopicConfigs(zkUtils);
		for (scala.collection.Iterator<Tuple2<String, Properties>> it = topics.iterator(); it.hasNext();) {
			Tuple2<String,Properties> topic = it.next();
			System.out.println("Topic: " + topic._1);
		}
	}
	
	public static void close() {
		zkClient.close();
	}

}