
import java.util.logging.Logger;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import scala.collection.script.Start;
 
public class ConsumerThread implements Runnable {
	
	private static Logger _log = Logger.getLogger(Start.class.getName());
	
    @SuppressWarnings("rawtypes")
	private KafkaStream m_stream;
    private int m_threadNumber;
 
    @SuppressWarnings("rawtypes")
	public ConsumerThread(KafkaStream a_stream, int a_threadNumber) {
        m_threadNumber = a_threadNumber;
        m_stream = a_stream;
    }

	@SuppressWarnings("unchecked")
	@Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator();
        while (it.hasNext()) {
        	String message = new String(it.next().message());
        	if (Config.DEBUG_MODE == true ) _log.info("Thread " + m_threadNumber + ": " + message);
        	SysUtils.saveImage(message);
        }
        System.out.println("Shutting down Thread: " + m_threadNumber);
    }
 
}