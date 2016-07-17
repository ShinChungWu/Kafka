import java.io.File;


/**
 * @filename MisoKafka.java
 * @author AkiraWu (akira.wu@gmail.com)
 * @version 1.0.0
 */

public class MisoKafka {

	private static void useMsg() {
		System.err.println("Usage: java -jar MisoKafka.jar [producer|consumer] [imagePath|savePath] [debug]");
		System.err.println("Ex: java -jar MisoKafka.jar producer /tmp/image/demo.jpg");
		System.err.println("Ex: java -jar MisoKafka.jar consumer /save");
		System.exit(0);
	}
	static File file;	
	public static void main(String[] args) {

		if (args.length < 2) useMsg();
		if (!args[0].equals("producer") && !args[0].equals("consumer")) useMsg();
		if (args.length == 3) if (args[2].equals("debug")) Config.DEBUG_MODE = true;
		
		if (args[0].equals("producer")) {
			try {
				SysUtils.checkImage(args[1]);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				System.exit(0);
			}
			
			File file = new File(args[1]);
			String imageDataString = SysUtils.readImage(file);
			KafkaUtils.createTopic(Config.TOPIC);

			if (Config.DEBUG_MODE == true ) {
				KafkaUtils.listTopic();
				KafkaUtils.close();
			}
			
			Producer producer = new Producer(Config.TOPIC, imageDataString, Config.SYNCHRONOUSLY);
			producer.start();
		}
		
		if (args[0].equals("consumer")) {
			Config.ImageSavePath = args[1];
			KafkaUtils.createTopic(Config.TOPIC);
			Consumer consumer = new Consumer(Config.ZOOKEEPER_SERVER, Config.CONSUMER_CLIENT_ID, Config.TOPIC);
			consumer.run(Config.KAFKA_CONSUMER_THREADS);
	        /*
        	try {
            	Thread.sleep(10000);
        	} catch (InterruptedException e) {
 				e.getMessage();
        	}
        	consumer.shutdown();
        	*/
		}
		
	}

}