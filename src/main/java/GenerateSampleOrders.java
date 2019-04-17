import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.voltdb.client.Client;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.NullCallback;

public class GenerateSampleOrders {

	
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
	
	public static void main(String[] args ) throws UnknownHostException, IOException {
		
		Client client = ClientFactory.createClient();
		client.createConnection("localhost");
		
		Random random = ThreadLocalRandom.current();
		
		for(int i=1000000; i>0; i--) {
			String item = SYMBOLS[random.nextInt(SYMBOLS.length)];
			int price = BASE_MAP.get(item);
			System.out.println(i + ":" + item + ": " + price);
			client.callProcedure(new NullCallback(), "newExecOrder", i, item, price);
		}
	}
}
