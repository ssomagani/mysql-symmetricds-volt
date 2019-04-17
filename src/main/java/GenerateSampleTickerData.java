import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * 
 */

/**
 * @author seetasomagani
 *
 */
public class GenerateSampleTickerData {

    private final static String TOPIC = "test-1";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092";
    
    
	public static void main(String[] args) throws Exception {
		runProducer(1);
	}

	private static Producer<String, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                            BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                        StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        			StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }
	
	static final String[] SYMBOLS = {"IBM", "GOOG", "AMZN", "WTM", "AAPL", "SEB", 
			"BUD", "BKNG", "ABBV", "GWPH", "CRM", "LOW", "FANG"};
	static final HashMap<String, Integer> BASE_MAP = new HashMap<>();
	
	static {
		BASE_MAP.put("IBM", 120);
		BASE_MAP.put("GOOG", 1200);
		BASE_MAP.put("AMZN", 1800);
		BASE_MAP.put("WTM", 923);
		BASE_MAP.put("AAPL", 200);
		BASE_MAP.put("SEB", 4300);
		BASE_MAP.put("BUD", 90);
		BASE_MAP.put("BKNG", 1700);
		BASE_MAP.put("ABBV", 80);
		BASE_MAP.put("GWPH", 170);
		BASE_MAP.put("CRM", 160);
		BASE_MAP.put("LOW", 120);
		BASE_MAP.put("FANG", 100);
	}
	
	static void runProducer(final int sendMessageCount) throws Exception {
	      final Producer<String, String> producer = createProducer();
	      Random random = ThreadLocalRandom.current();
	      
	      try {
	          for (int index=0; index<10000000; index++) {
	        	  long time = new Date().getTime();
	        	  
	        	  String symbol = SYMBOLS[random.nextInt(SYMBOLS.length)];
	              final ProducerRecord<String, String> record =
	                      new ProducerRecord<>(TOPIC, symbol, symbol + "," + (BASE_MAP.get(symbol) + random.nextInt(30)));

	              RecordMetadata metadata = producer.send(record).get();

	              long elapsedTime = System.currentTimeMillis() - time;
	              System.out.println("----------------------------------------------------------------------");
	              System.out.printf("sent record(key=%s value=%s) " +
	                              "meta(partition=%d, offset=%d) time=%d\n",
	                      record.key(), record.value(), metadata.partition(),
	                      metadata.offset(), elapsedTime);

	          }
	      } catch (Exception e) {
	    	  e.printStackTrace();
	      } finally {
	          producer.flush();
	          producer.close();
	      }
	  }
}
