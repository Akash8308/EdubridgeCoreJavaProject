package atmclasses;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class DbOperations {
	
	private static Connection con=DatabaseConnection.getConnection();
	private static PreparedStatement stmt;
	Scanner sc = new Scanner(System.in);
	private static ResultSet rs;
	private static String query; 
	
	//get username
	static String getUser() throws SQLException {
//		con =  DatabaseConnection.getConnection();
		stmt = con.prepareStatement("select uname from login where uid=?");
		stmt.setInt(1, LoginSession.getUserId());
		rs = stmt.executeQuery();
		while(rs.next()) {
			return rs.getString(1);			
		}
		return "";
	}
	
	
	// Get balance
	static int getBalance() throws SQLException {
//		con =  DatabaseConnection.getConnection();
		stmt = con.prepareStatement("select balance from user where uid=?");
		stmt.setInt(1, LoginSession.getUserId());
		rs = stmt.executeQuery();
		while(rs.next()) {
			return rs.getInt(1);			
		}
		return -1;
		
	}

	public static Boolean checkRecord(String uemail) throws SQLException {
		query = ("Select * from employee where eemail =" + "'" + uemail +"'");
		rs = stmt.executeQuery(query);
		if(rs==null)
			return false;
		return true;
		
	}
	
	// Validate user login
	public static boolean validateUser(int userID, int pass) throws SQLException {
		stmt = con.prepareStatement("select uid,upass from login where uid=?");
		stmt.setInt(1,userID);
		rs = stmt.executeQuery();
		while(rs.next()) {
			//Validate userID
			if(userID == rs.getInt(1)) {
				//Validate userPassword
				if(pass == (rs.getInt(2)))
					return true;
				else {
					System.err.println("Invalid PIN");
					return false;
				}
					
			}	
			else {
				System.err.println("Invalid user ID");
				return false;
			}
			
		}
		return false;
	}

	//Deposit
	public void deposit() throws SQLException {
		System.out.println("Enter Ammount: ");
		int depAmmount = sc.nextInt();
		System.out.println("Enter Pin: ");
		int pin = sc.nextInt();
		if(depAmmount<0) {
			System.err.println("Invalid ammount");
			return;
		}
		else if(validateUser(LoginSession.getUserId(), pin)) {
			
		int currentBal = getBalance();
		
		stmt = con.prepareStatement("update user set balance=? where uid=?");
		stmt.setInt(1, (currentBal+ depAmmount ));
		stmt.setInt(2, LoginSession.getUserId());
		
		stmt.executeUpdate();
		
		//update transaction history
		addTransaction("Deposit",depAmmount);

		}
		
	}
	
	// Register Transaction
	private static void addTransaction(String transactionType, int depAmmount) throws SQLException {
		con = DatabaseConnection.getConnection();		
		int bal =  getBalance();
		stmt = con.prepareStatement("insert into transactions(transaction_type,uid,balance, ammount) values(?,?,?,?)");
		stmt.setString(1, transactionType);
		stmt.setInt(2, LoginSession.getUserId());
		stmt.setInt(3,bal);
		stmt.setInt(4, depAmmount);
		
		int i = stmt.executeUpdate();
		if(i==0)
			System.err.println("Error code: TSC101");
		
		System.out.println("Current balance: " + getBalance());	
	}
	

	public void withdraw() throws SQLException {
		System.out.println("Enter Ammount: ");
		int withdrawAmmount = sc.nextInt();
		System.out.println("Enter Pin: ");
		int pin = sc.nextInt();
		
		if(withdrawAmmount<0) {
			System.err.println("Invalid ammount");
			return;
		}
		int currentBal = getBalance();
		
		if(currentBal-withdrawAmmount < 0) {
			System.out.println("Not Enough balance");
		}
		else if(validateUser(LoginSession.getUserId(), pin))
		{
			stmt = con.prepareStatement("update user set balance=? where uid=?");
			stmt.setInt(1,currentBal-withdrawAmmount);
			stmt.setInt(2, LoginSession.getUserId());
			stmt.executeUpdate();
			
			addTransaction("Withdraw",withdrawAmmount);
			
		}
	}

	public void miniStatement() throws SQLException {
		stmt = con.prepareStatement("select transaction_type,transaction_time,ammount,balance from transactions where uid=? order by transaction_id desc limit 10");
		stmt.setInt(1, LoginSession.getUserId());
		rs = stmt.executeQuery();
		if(rs!=null) {
			 System.out.println("+----------------------+---------------------------+-----------------+--------------+");
		     System.out.printf("| %-20s | %-25s | %-15s | %-12s |\n", "Transaction_type", "Transaction_time", "Amount", "Balance");
		     System.out.println("+----------------------+---------------------------+-----------------+--------------+");
	     }
		else
			System.err.println("No Transaction History");

		while(rs.next()) {
			if(rs.getString(1).equals("Deposit"))
				System.out.printf("| %-20s | %-25s | \u001B[32m%-15s\u001B[0m | %-12s |\n", rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
			else
				System.out.printf("| %-20s | %-25s | \u001B[31m%-15s\u001B[0m | %-12s |\n", rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
		}
		System.out.println("+----------------------+---------------------------+-----------------+--------------+");
		
	}
	public static boolean validatePIN(String str) {
		String regex = "\\d{4}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	public void changePin() throws SQLException {
		//Get new pin
		System.out.println("Enter old PIN");
		String oldPin = sc.next(); 
		if(!validatePIN(oldPin)) {
			System.err.println("Error");
		}
		System.out.println("Enter new PIN");
		String newPin = sc.next();
		if(!validatePIN(newPin)) {
			System.err.println("Error");
			return;
		}
		if(Integer.parseInt(newPin)==Integer.parseInt(oldPin))
		{
			System.out.println("New PIN cannot be same as used PIN");
			return;
			
		}
		stmt = con.prepareStatement("select upass from login where uid=?");
		stmt.setInt(1,LoginSession.getUserId());
		try {
			rs = stmt.executeQuery();
			int dboldPin=0;
			while(rs.next())
				dboldPin = rs.getInt(1);

		//Check password match or not
		if(dboldPin==Integer.parseInt(oldPin)) {
			//update pin query
			stmt = con.prepareStatement("update login set upass=? where uid=?");
			stmt.setInt(1, Integer.parseInt(newPin));
			stmt.setInt(2, LoginSession.getUserId());
			int i=0;
			try {
				i = stmt.executeUpdate();
				
			} catch (Exception e) {
					e.printStackTrace();
			}
			if(i!=0)
				System.out.println("PIN changed successfully");
			else
				System.err.println("something went wrong");
		}
		else
			System.err.println("Old Pin Does not match");
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	
}
