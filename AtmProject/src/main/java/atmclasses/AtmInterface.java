package atmclasses;

import java.sql.SQLException;
import java.util.Scanner;

public class AtmInterface {
	static Scanner sc;

	void userInterface() throws SQLException {
		Scanner sc = new Scanner(System.in);
		DbOperations dbOps = new DbOperations();

		System.out.println(
				"\n--------------------------------------------------------------------- Welcome ---------------------------------------------------------------------");
		int ch = 0;
		while (true) {
			// ATM HomePage
			System.out.println("\n1: Generate PIN \n2: Existing user\n3: Stop");
			ch = sc.nextInt();
			if (ch == 1) {
				System.out.println("OTP Generated");
			} else if (ch == 3) {
				System.out.println(
						"Terminating program\n--------------------------------------------------------------------- Thank You! ---------------------------------------------------------------------");
				System.exit(0);
			} else {
				if (LoginSession.userLogin(sc)) {
					System.out.println("Welcome " + DbOperations.getUser());
					while (ch != 6) {
						// Banking Menu
						System.out.println(
								"\n1: Deposit \t\t2: Withdraw \n3: Check Balance \t4: Mini Statement \n5: Change PIN\t\t6: LogOut");
						ch = sc.nextInt();
						switch (ch) {
							case 1:	dbOps.deposit();
										break;
							case 2: dbOps.withdraw();
										break;
							case 3: System.out.println("Current Accound Balance: " + dbOps.getBalance());
										break;
							case 4:	dbOps.miniStatement();
										break;
							case 5:	dbOps.changePin();
										break;
							case 6:	LoginSession.logOut();
										break;
							default:
								System.out.println("Invalid choice");
						}
					}
				} else {
					System.out.println("Invalid UserID or Password");
				}
			}
		}
	}
}
