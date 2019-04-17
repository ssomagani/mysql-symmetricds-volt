import java.sql.{Connection,DriverManager}

object ScalaJdbcConnectSelect extends App {
    // connect to the database named "mysql" on port 8889 of localhost
    val url = "jdbc:h2:tcp://54.82.2.83:orders;AUTO_SERVER=TRUE;LOCK_TIMEOUT=60000"
    val driver = "org.h2.Driver"
    val username = "symmetric"
    val password = ""
    var connection:Connection = _
    try {
        Class.forName(driver)
        connection = DriverManager.getConnection(url, username, password)
        val statement = connection.createStatement
        val rs = statement.executeQuery("SELECT * FROM order_item")
        while (rs.next) {
            val host = rs.getString("host")
            val user = rs.getString("user")
            println("host = %s, user = %s".format(host,user))
        }
    } catch {
        case e: Exception => e.printStackTrace
    }
    connection.close
}