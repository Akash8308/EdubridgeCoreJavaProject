package atmclasses;

import java.sql.SQLException;
import java.util.Scanner;

public class LoginSession {

	private static int userId;
	private static int pass;
	private boolean login=false;
	private static int balance;

	public static void setBalance() throws SQLException {
		balance = DbOperations.getBalance();
	}
	
	public static int getUserId() {
		return userId;
	}

	public static void setUserId(int uId) throws SQLException {
		userId = uId; 
	}

	public static int getBalance() {
		return balance;
	}
	
	public static boolean userLogin(Scanner sc) throws SQLException {
		//get user id and password
		
		System.out.println("Enter userID");
		userId = sc.nextInt();
		System.out.println("Enter Password");
		pass = sc.nextInt();
		

		// validate userId & password
		if(DbOperations.validateUser(userId,pass))
		{
			LoginSession.setUserId(userId);
			return true;
		}
		
		return false;
	}
	
	public static void logOut() {
		try {
			LoginSession.setUserId(0);
			System.out.println("Logged Out\nThank you!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

