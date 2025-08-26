/**
****************************CORE JAVA PROJECT********************
* CSR CAPGEMINI TRAINING PROJECT
* EDUBRIDGE INDIA PRIVATE LIMITED
* PROJECT TITLE: ATM Interface Project
* UNDER THE GUIDENCE OF TRAINER MRS.INDRAKKA MALLI
* @DONE BY AKASH HEDAU
* In ATM Interface Project:
MAIN OPERATIONS:
* USER LOGIN → user can perform this operation. *LOGIN → 
* Deposit Money→ user can perform this operation. 
* Withdraw → user can  withdraw money from their bank account.
* Check balance →  user can  check their bank balance
* mini-statement → user can check their last 10 transaction. */

//Program Start
package atmclasses;

import java.sql.SQLException;

public class AtmMain {

	public static void main(String[] args) throws SQLException {
		// Initialise
		AtmInterface intface = new AtmInterface();

		intface.userInterface();

	}

}
