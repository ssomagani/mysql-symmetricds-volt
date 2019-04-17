import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateSampleData {
	
	public static void main(String[] args) throws SQLException {
		String url = "jdbc:h2:file:~/symmetric-server-3.10.0/tmp/h2/orders;AUTO_SERVER=TRUE;LOCK_TIMEOUT=60000";
		String driver = "org.h2.Driver";
		String username = "symmetric";
		String password = "";
		Connection connection = null;
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT count(*) FROM order_item ");
			int order_id = 0;
			if (rs.next()) {
				order_id = rs.getInt(1);
				System.out.println("max order_id is " + order_id);
			}
			
			for (;;) {
				String item = "";
				int price = 0;
				if(ThreadLocalRandom.current().nextBoolean()) {
					item = "IBM";
					price = 120 + ThreadLocalRandom.current().nextInt(30);
				} else {
					item = "GOOG";
					price = 1200 + ThreadLocalRandom.current().nextInt(120);
				}
				statement.execute("insert into order_item values (" + ++order_id + ",'" + item + "')");
				statement.execute("insert into order_price values (" + order_id + ", " + price + ")");
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		connection.close();
	}
}
