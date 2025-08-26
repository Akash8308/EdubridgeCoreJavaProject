package atmclasses;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	private static String jdbcDriver = "com.mysql.cj.jdbc.Driver";
	private static String url="jdbc:mysql://localhost:3306/atmdatabase";
	private static String userName = "root";
	private static String userPass = "root";
	private static Connection con=null;
	
	private DatabaseConnection() {}
	
	public static  Connection getConnection() {
		
		
		if(con==null) {
			try {
		
				Class.forName(jdbcDriver);
				con = DriverManager.getConnection(url,userName,userPass);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else
			return con;
		return con;
	}
}
