/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import scala.collection.script.Start;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Producer extends Thread {
	
	private static Logger _log = Logger.getLogger(Start.class.getName());
	private final KafkaProducer<Integer, String> producer;
    private final String topic;
    private final Boolean isSync;
    private String message;

    public Producer(String topic, String data, Boolean isSyync) {
        Properties props = new Properties();
        props.put("bootstrap.servers", Config.KAFKA_SERVER);
        props.put("retries", Config.RETRIES);
        props.put("client.id", Config.PRODUCER_CLIENT_ID);
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
        this.topic = topic;
        this.message = data;
        this.isSync = isSyync;
    }

    public void run() {
//    	for ( int i=1 ; i<10 ; i++) {
    	long startTime = System.currentTimeMillis();
    	if (isSync) { // Send synchronously
    		try {
    			producer.send(new ProducerRecord<>(topic, message)).get();
    			_log.info("Sent message: done");
    			if (Config.DEBUG_MODE == true ) System.out.println("Sent message: (" + message + ")");
    		} catch (InterruptedException | ExecutionException e) {
    			e.printStackTrace();
    		}
    	} else { // Send asynchronously
    		producer.send(new ProducerRecord<>(topic, message), new DemoCallBack2(startTime, message));
    	}
    	}
//    }
}

class DemoCallBack2 implements Callback {

    private final long startTime;
    private final String message;

    public DemoCallBack2(long startTime, String message) {
        this.startTime = startTime;
        this.message = message;
    }

    /**
     * A callback method the user can implement to provide asynchronous handling of request completion. This method will
     * be called when the record sent to the server has been acknowledged. Exactly one of the arguments will be
     * non-null.
     *
     * @param metadata  The metadata for the record that was sent (i.e. the partition and offset). Null if an error
     *                  occurred.
     * @param exception The exception thrown during processing of this record. Null if no error occurred.
     */
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            System.out.println(
                "message(" + ", " + message + ") sent to partition(" + metadata.partition() +
                    "), " +
                    "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            exception.printStackTrace();
        }
    }
}
