package ibm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import ibm.db.DB;
import ibm.db.DB.TransBy;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.InputException;
import ibm.resource.Message;
import ibm.resource.Transaction;
import ibm.resource.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class TestDB extends Mockito {
	
	private static Connection connection;
	
	@BeforeClass
	public static void setUp() throws SQLException{
		Properties properties = new Properties();
		properties.put("user", "DTU18");
		properties.put("password", "FAGP2016");
		properties.put("retreiveMessagesFromServerOnGetMessage", "true");
		properties.put("emulateParameterMetaDataForZCalls", "1");
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		try {
			DB.getConnection(true);
		} finally {
			//Close connection.
			if (connection != null) connection.close();
		}
	}
	
	@AfterClass
	public static void cleanUp() throws SQLException{
		Properties properties = new Properties();
		properties.put("user", "DTU18");
		properties.put("password", "FAGP2016");
		properties.put("retreiveMessagesFromServerOnGetMessage", "true");
		properties.put("emulateParameterMetaDataForZCalls", "1");
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		try {
			connection = DriverManager.getConnection("jdbc:db2://192.86.32.54:5040/DALLASB", properties);
			DB.setConnection(connection);

			//Delete everything!
			Statement deleteTransactions = null;
			try {
				deleteTransactions = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				deleteTransactions.executeUpdate("DELETE FROM DTUGRP07.TRANSACTIONS "
						+ "WHERE DESCRIPTION LIKE 'Test%';");
			} finally {
				if (deleteTransactions != null) deleteTransactions.close();
			}
			Statement deleteArchiveTransactions = null;
			try {
				deleteArchiveTransactions = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				deleteArchiveTransactions.executeUpdate("DELETE FROM DTUGRP07.ARCHIVE "
						+ "WHERE DESCRIPTION LIKE 'Test%';");
			} finally {
				if (deleteArchiveTransactions != null) deleteArchiveTransactions.close();
			}
			Statement updateBalance = null;
			try {
				updateBalance = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				updateBalance.executeUpdate("UPDATE DTUGRP07.ACCOUNTS "
						+ "SET BALANCE = '0' "
						+ "WHERE NAME LIKE 'Test%';");
			} finally {
				if (updateBalance != null) updateBalance.close();
			}
			Statement deleteAccount = null;
			try {
				deleteAccount = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				deleteAccount.executeUpdate("DELETE FROM DTUGRP07.ACCOUNTS "
						+ "WHERE NAME LIKE 'Test%';");
			} finally {
				if (deleteAccount != null) deleteAccount.close();
			}
			Statement deleteUser = null;
			try {
				deleteUser = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				deleteUser.executeUpdate("DELETE FROM DTUGRP07.USERS "
						+ "WHERE USERNAME LIKE 'Test%';");
			} finally {
				if (deleteUser != null) deleteUser.close();
			}
			Statement deleteMessages = null;
			try {
				deleteMessages = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				deleteMessages.executeUpdate("DELETE FROM DTUGRP07.INBOX "
						+ "WHERE MESSAGE LIKE 'Test%';");
			} finally {
				if (deleteMessages != null) deleteMessages.close();
			}

		} finally {
			//Close connection.
			if (connection != null) connection.close();
		}
	}
	
	@Test
	public void testCreateMethods() throws DatabaseException {
		//Test Create User
		String username = "TestCreateUser";
		String cpr = "TestCU1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Attempt to get a user that doesn't yet exist.
		User nullUser = DB.getUserByCpr(cpr);
		assertNull(nullUser);
		//Create user
		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);
		//Get the newly created user
		User sameUser = DB.getUserByCpr(cpr);
		assertNotNull(sameUser);
		//Assertion
		assertTrue(0 < user.getId());
		assertEquals(user.getId(), sameUser.getId());
		assertEquals(user.getUsername(), sameUser.getUsername());
		assertEquals(user.getName(), sameUser.getName());
		assertEquals(user.getCpr(), sameUser.getCpr());
		assertEquals(user.getInstitute(), sameUser.getInstitute());
		assertEquals(user.getConsultant(), sameUser.getConsultant());
		
		//Test Create Account
		int userId = user.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestCA123";
		String iban = "Test123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		//Get not existing account
		Account nullAccount = DB.getAccountByNumber(number);
		assertNull(nullAccount);
		//Create Account
		Account account = DB.createAccount(userId, accountName, type, number, iban, currency, interest);
		assertNotNull(account);
		//Get newly created account
		Account sameAccount = DB.getAccountByNumber(number);
		//Assertion
		assertEquals(account.getId(), sameAccount.getId());
		assertEquals(account.getBalance(), sameAccount.getBalance());
		assertEquals(account.getCurrency(), sameAccount.getCurrency());
		assertEquals(account.getIban(), sameAccount.getIban());
		assertEquals(account.getInterest(), sameAccount.getInterest());
		assertEquals(account.getName(), sameAccount.getName());
		assertEquals(account.getNumber(), sameAccount.getNumber());
		assertEquals(account.getType(), sameAccount.getType());
		assertEquals(account.getUserId(), sameAccount.getUserId());
		
		//Test Create Transaction
		int accountId = account.getId();
		String description = "TestDecription is this, test-test, moo...";
		double amount = 100;
		//Get empty list of transactions
		ArrayList<Transaction> emptyTransactions = DB.getTransactions(accountId);
		assertTrue(emptyTransactions.isEmpty());
		//Create Transaction
		long date = Calendar.getInstance().getTime().getTime();
		assertTrue(DB.createTransaction(accountId, description, amount, currency));
		ArrayList<Transaction> transaction = DB.getTransactions(accountId);
		//Assertion
		assertEquals(transaction.get(0).getAccountId(), accountId);
		assertEquals(transaction.get(0).getAmount(), new DecimalFormat("#0.00").format(amount));
		assertEquals(transaction.get(0).getDate(), new SimpleDateFormat("dd.MM.yyyy").format(date));
		assertEquals(transaction.get(0).getDescription(), description);
	}
	
	@Test
	public void testCheckLogin() throws DatabaseException, InputException {
		//Test Check Login
		String username = "TestCreateLogin";
		String password = "password";
		String cpr = "TestCL1234";
		String name = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Attempt to get a user that doesn't yet exist.
		User nullUser = DB.getUserByCpr(cpr);
		assertNull(nullUser);
		//Create user
		User user = DB.createUser(username, cpr, name, institute, consultant);
		assertNotNull(user);
		//Check login
		int id = DB.checkLogin(username, password);
		//Assert Id is equal
		assertEquals(user.getId(), id);
	}
	
	@Test
	public void testGetUser() throws DatabaseException{
		//Test Get User
		String username = "TestGetUser";
		String cpr = "TestGS1234";
		String name = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create a user to get
		User user = DB.createUser(username, cpr, name, institute, consultant);
		assertNotNull(user);
		//Get user
		User sameUser = DB.getUser(user.getId());
		assertNotNull(sameUser);
		//Assert
		assertEquals(user.getId(), sameUser.getId());
		assertEquals(user.getConsultant(), sameUser.getConsultant());
		assertEquals(user.getCpr(), sameUser.getCpr());
		assertEquals(user.getInstitute(), sameUser.getInstitute());
		assertEquals(user.getName(), sameUser.getName());
		assertEquals(user.getUsername(), sameUser.getUsername());
	}
	
	@Test
	public void testGetUsers() throws DatabaseException{
		//Test Get Users
		String username = "TestGetUsers";
		String cpr = "TestGM1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		for (int i = 1; i < 15; i++){
			assertNotNull(DB.createUser(username+i, cpr+i, userName+i, institute, consultant));
		}
		//Get Users
		ArrayList<User> users = DB.getUsers(0);
		assertFalse(users.isEmpty());
		//Assertion
		for (User user : users){
			assertNotNull(user.getId());
			assertNotNull(user.getCpr());
			assertNotNull(user.getName());
		}
		assertTrue(users.size() == 10);
		//Get Users
		users.addAll(DB.getUsers(10));
		assertFalse(users.isEmpty());
		//Assertion
		for (User user : users){
			assertNotNull(user.getId());
			assertNotNull(user.getCpr());
			assertNotNull(user.getName());
		}
		assertTrue(users.size() > 1);
	}

	@Test
	public void testGetTransactions() throws DatabaseException{
		//Test Get Transactions
		//Create User
		String username = "TestGetTrans";
		String cpr = "TestGT1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);		
		//Create Account
		int userId = user.getId();
		String accountName = "TestGetTrans";
		String type = "TestTypeForTestAccount";
		String number = "TestGT123456789"; 
		String iban = "TestGT123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		//Create Account
		Account account = DB.createAccount(userId, accountName, type, number, iban, currency, interest);
		assertNotNull(account);
		//Create Transaction
		int accountId = account.getId();
		String description = "TestDecription is this, test-test, moo..."; 
		double amount = 100;
		//Create Transaction
		assertNotNull(DB.createTransaction(accountId, description, amount, currency));
		//Get Transactions
		ArrayList<Transaction> transactions = DB.getTransactions(accountId);
		assertFalse(transactions.isEmpty());
		//Assertion
		for (Transaction transaction : transactions){
			assertNotNull(transaction.getAccountId());
			assertNotNull(transaction.getAmount());
			assertNotNull(transaction.getDate());
			assertNotNull(transaction.getDateRaw());
			assertNotNull(transaction.getDescription());
		}
	}

	@Test
	public void testGetAccounts() throws DatabaseException{
		//Test Get Accounts
		//Create User
		String username = "TestGetAccount";
		String cpr = "TestGA1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);
		//Create Account
		int userId = user.getId();
		String accountName = "TestGetAccount";
		String type = "TestTypeForTestAccount";
		String number = "TestGA123456789"; 
		String iban = "TestGA123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		//Create Account
		Account newAccount = DB.createAccount(userId, accountName, type, number, iban, currency, interest);
		assertNotNull(newAccount);
		//Get Accounts
		ArrayList<Account> accounts = DB.getAccounts(userId);
		assertNotNull(accounts);
		//Assertion
		for (Account account : accounts){
			assertNotNull(account.getBalance());
			assertNotNull(account.getCurrency());
			assertNotNull(account.getIban());
			assertNotNull(account.getId());
			assertNotNull(account.getInterest());
			assertNotNull(account.getName());
			assertNotNull(account.getNumber());
			assertNotNull(account.getType());
			assertNotNull(account.getUserId());
			//Range assertion
			assertTrue(0 < account.getId());
			assertTrue(0 < account.getUserId());
			assertTrue(0.0 == Double.parseDouble(account.getBalance().replace(',', '.')));
		}
	}
	
	@Test
	public void testUpdateAccount() throws DatabaseException {
		//Test Update Account
		//Create User
		String username = "TestUpdateAccount";
		String cpr = "TestUA1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user

		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);
		
		//Create Account
		int userId = user.getId();
		String accountName = "TestUpdateAccount";
		String type = "TestTypeForTestAccount";
		String number = "TestUA123456789"; 
		String iban = "TestUA123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Create Account

		Account newAccount = DB.createAccount(userId, accountName, type, number, iban, currency, interest);
		assertNotNull(newAccount);
		int accountId = newAccount.getId();
		//Assert original values.
		assertEquals(userId, newAccount.getUserId());
		assertEquals(new DecimalFormat("#0.00").format(balance), newAccount.getBalance());
		assertEquals(currency, newAccount.getCurrency());
		assertEquals(iban, newAccount.getIban());
		assertEquals(new DecimalFormat("#0.00").format(interest)+"%", newAccount.getInterest());
		assertEquals(accountName, newAccount.getName());
		assertEquals(number, newAccount.getNumber());
		//Update Account 
		String newAccountName = "TestUpdateAccountNew";
		assertNotNull(DB.updateAccount(accountId, newAccountName, DB.ACCOUNT.NAME));
		double newBalance = 100;
		assertNotNull(DB.updateAccount(accountId, ""+newBalance, DB.ACCOUNT.BALANCE));
		String newIban = "TestUAN123456IBAN";
		assertNotNull(DB.updateAccount(accountId, newIban, DB.ACCOUNT.IBAN));
		double newInterest = 0.10;
		assertNotNull(DB.updateAccount(accountId, ""+newInterest, DB.ACCOUNT.INTEREST));
		String newNumber = "TestUAN123456789";
		assertNotNull(DB.updateAccount(accountId, newNumber, DB.ACCOUNT.NUMBER));
		String newType = "TestNewTypeForTestAccount";
		assertNotNull(DB.updateAccount(accountId, newType, DB.ACCOUNT.TYPE));
		//Assertion
		Account updatedAccount = DB.getAccount(accountId);
		assertEquals(userId, updatedAccount.getUserId());
		assertEquals(new DecimalFormat("#0.00").format(newBalance), updatedAccount.getBalance());
		assertEquals(currency, updatedAccount.getCurrency());
		assertEquals(newIban, updatedAccount.getIban());
		assertEquals(new DecimalFormat("#0.00").format(newInterest)+"%", updatedAccount.getInterest());
		assertEquals(newAccountName, updatedAccount.getName());
		assertEquals(newNumber, updatedAccount.getNumber());
		
		//Update Account 
		String newNewAccountName = "TestUpdateAccountNew";
		String newNewIban = "TestUAN123456IBAN";
		double newNewInterest = 0.10;
		String newNewNumber = "TestUAN123456789";
		String newNewType = "TestNewTypeForTestAccount";
		assertNotNull(DB.updateAccount(accountId, newNewAccountName, newNewType, newNewNumber, newNewIban, newNewInterest));
		//Assertion
		Account updatedUpdatedAccount = DB.getAccount(accountId);
		assertEquals(userId, updatedUpdatedAccount.getUserId());
		assertEquals(newNewIban, updatedUpdatedAccount.getIban());
		assertEquals(new DecimalFormat("#0.00").format(newNewInterest)+"%", updatedUpdatedAccount.getInterest());
		assertEquals(newNewAccountName, updatedUpdatedAccount.getName());
		assertEquals(newNewNumber, updatedUpdatedAccount.getNumber());
	}

	@Test
	public void testUpdateUser() throws DatabaseException {
		//Test Update User
		//Create User
		String username = "TestUpdateUser";
		String password = "password";
		String cpr = "TestUU1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user

		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);
		
		//Assert original values.
		assertEquals(consultant, user.getConsultant());
		assertEquals(cpr, user.getCpr());
		assertTrue(0 < user.getId());
		assertEquals(institute, user.getInstitute());
		assertEquals(userName, user.getName());
		assertEquals(username, user.getUsername());
		assertTrue(0 < DB.checkLogin(username, password));
		
		//Update User
		String newusername = "TestNewUpdateUser";
		String newpassword = "TestN1234";
		String newcpr = "TestUUN1234";
		String newuserName = "New Test Testy Test";
		String newinstitute = "New Test That Institute";
		String newconsultant = "Tommy";
		assertNotNull(DB.updateUser(user.getId(), newusername, USER.USERNAME));
		assertNotNull(DB.updateUser(user.getId(), newuserName, USER.NAME));
		assertNotNull(DB.updateUser(user.getId(), newpassword, USER.PASSWORD));
		assertNotNull(DB.updateUser(user.getId(), newconsultant, USER.CONSULTANT));
		assertNotNull(DB.updateUser(user.getId(), newcpr, USER.CPR));
		assertNotNull(DB.updateUser(user.getId(), newinstitute, USER.INSTITUTE));
		
		//Assertion
		User updatedUser = DB.getUser(user.getId());
		assertEquals(user.getId(), updatedUser.getId());
		assertEquals(newconsultant, updatedUser.getConsultant());
		assertEquals(newcpr, updatedUser.getCpr());
		assertEquals(newinstitute, updatedUser.getInstitute());
		assertEquals(newuserName, updatedUser.getName());
		assertEquals(newusername, updatedUser.getUsername());
		assertFalse(0 < DB.checkLogin(username, password));
		assertFalse(0 < DB.checkLogin(newusername, password));
		assertFalse(0 < DB.checkLogin(username, newpassword));
		assertTrue(0 < DB.checkLogin(newusername, newpassword));
		
		//Update User 
		String newnewusername = "TestNewNewUpdateUser";
		String newnewcpr = "TestUUNN1234";
		String newnewuserName = "New Moo Testy Test";
		String newnewinstitute = "New Moo That Institute";
		String newnewconsultant = "Tommy the 2rd";
		//Assertion
		User updatedUpdatedUser = DB.updateUser(user.getId(), newnewusername, newnewcpr, newnewuserName, newnewinstitute, newnewconsultant);
		assertNotNull(updatedUpdatedUser);
		assertEquals(user.getId(), updatedUpdatedUser.getId());
		assertEquals(newnewconsultant, updatedUpdatedUser.getConsultant());
		assertEquals(newnewcpr, updatedUpdatedUser.getCpr());
		assertEquals(newnewinstitute, updatedUpdatedUser.getInstitute());
		assertEquals(newnewuserName, updatedUpdatedUser.getName());
		assertEquals(newnewusername, updatedUpdatedUser.getUsername());
		assertFalse(0 < DB.checkLogin(newusername, newpassword));
		assertTrue(0 < DB.checkLogin(newnewusername, newpassword));
		
		//Reset Password
		DB.resetPassword(user.getId());
		//Assertion
		assertFalse(0 < DB.checkLogin(newnewusername, newpassword));
		assertTrue(0 < DB.checkLogin(newnewusername, "password"));
	}
	
	@Test
	public void testDeleteMethods() throws DatabaseException {
		//Test Delete User
		//Create user
		String username = "TestDeleteUserByCpr";
		String cpr = "TestDUBC1234";
		String userName = "DUBC Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";

		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);
		
		//Create Account
		int userId = user.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestDABN123456789"; 
		String iban = "TestDABN123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;

		Account account = DB.createAccount(userId, accountName, type, number, iban, currency, interest);
		assertNotNull(account);
		
		//Delete Account
		assertTrue(DB.deleteAccountByNumber(number));
		assertNull(DB.getAccountByNumber(number));
		
		//Delete User
		User sameUser = DB.getUserByCpr(cpr);
		assertNotNull(sameUser);
		assertTrue(DB.deleteUserByCpr(cpr));
		assertNull(DB.getUserByCpr(cpr));
		
		//Create User
		String username2 = "TestDeleteUser";
		String cpr2 = "TestDU1234";
		String userName2 = "DU Test Testy Test";
		String institute2 = "Test That Institute";
		String consultant2 = "";

		User user2 = DB.createUser(username2, cpr2, userName2, institute2, consultant2);
		assertNotNull(user2);
		
		//Create Account
		int userId2 = user2.getId();
		String accountName2 = "TestAccountIsTest";
		String type2 = "TestTypeForTestAccount";
		String number2 = "TestDA123456789"; 
		String iban2 = "TestDA123456IBAN";
		String currency2 = "EUR"; 
		double interest2 = 0.05;

		Account account2 = DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2);
		assertNotNull(account2);
		
		//Delete Account
		assertTrue(DB.deleteAccount(account2.getId()));
		assertNull(DB.getAccount(account2.getId()));
		assertNull(DB.getAccountByNumber(number2));
		
		//Delete User
		User sameUser2 = DB.getUserByCpr(cpr2);
		assertNotNull(sameUser2);
		assertTrue(DB.deleteUser(sameUser2.getId()));
		assertNull(DB.getUser(sameUser2.getId()));
		assertNull(DB.getUserByCpr(cpr2));
	}
	
	@Test
	public void testCreateTransaction() throws DatabaseException {
		//Test Get Archive
		//Create user
		String username = "TestCreateTransaction1";
		String cpr = "TestCTT11234";
		String userName = "CTT1 Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
		User user1 = DB.getUserByCpr(cpr);
		assertNotNull(user1);
		
		//Create User
		String username2 = "TestCreatetransaction2";
		String cpr2 = "TestCTT21234";
		String userName2 = "CTT2 Test Testy Test";
		String institute2 = "Test That Institute";
		String consultant2 = "";
		assertNotNull(DB.createUser(username2, cpr2, userName2, institute2, consultant2));
		User user2 = DB.getUserByCpr(cpr2);
		assertNotNull(user2);
		
		//Create Account
		int userId = user1.getId();

		String accountName1 = "TestAccount1IsTest";
		String type1 = "TestTypeForTestAccount";
		String number1 = "TestCTT1123456789"; 
		String iban1 = "TestCTT1123456IBAN";
		String currency1 = "EUR"; 
		double interest1 = 0.1;
		double balance1 = 200;
		assertNotNull(DB.createAccount(userId, accountName1, type1, number1, iban1, currency1, interest1));
		Account account1 = DB.getAccountByNumber(number1);
		assertNotNull(account1);
		
		//Create Account
		int userId2 = user2.getId();
		String accountName2 = "TestAccount2IsTest";
		String type2 = "TestTypeForTestAccount";
		String number2 = "TestCTT2123456789"; 
		String iban2 = "TestCTT2123456IBAN";
		String currency2 = "EUR"; 
		double interest2 = 0.05;
		assertNotNull(DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2));
		Account account2 = DB.getAccountByNumber(number2);
		assertNotNull(account2);
		
		//Assert there are no transactions
		assertTrue(DB.getTransactions(account1.getId()).isEmpty());
		assertTrue(DB.getTransactions(account2.getId()).isEmpty());
		
		//Deposit to increase balance for further testing.
		assertTrue(DB.createTransaction(account1.getId(), "TestDeposit", balance1, currency1));
		assertEquals(200.0, Double.parseDouble(DB.getAccount(account1.getId()).getBalance().replace(',', '.')), 0.001);
		
		//Create transaction (ID)
		String description1 = "TestDecription1ID is this, test-test, moo...";
		String description2 = "TestDecription2ID is this, test-test, moo...";
		double amount = 100;
		String currency = "EUR";
		assertNotNull(DB.createTransaction(TransBy.ID, account1.getId(), ""+account2.getId(), description1, description2, -amount, currency));
		//Assertion
		ArrayList<Transaction> transactions1 = DB.getTransactions(account1.getId());
		assertFalse(transactions1.isEmpty());
		assertEquals(transactions1.get(0).getAccountId(), account1.getId());
		assertEquals(transactions1.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount));
		assertEquals(transactions1.get(0).getDescription(), description1);
		ArrayList<Transaction> transactions2 = DB.getTransactions(account2.getId());
		assertFalse(transactions2.isEmpty());
		assertEquals(transactions2.get(0).getAccountId(), account2.getId());
		assertEquals(transactions2.get(0).getAmount(), new DecimalFormat("#0.00").format(amount));
		assertEquals(transactions2.get(0).getDescription(), description2);
		
		//Check Balance
		assertEquals(100.0, Double.parseDouble(DB.getAccountByNumber(number1).getBalance().replace(',', '.')), 0.001);
		assertEquals(100.0, Double.parseDouble(DB.getAccountByNumber(number2).getBalance().replace(',', '.')), 0.001);
		
		//Create transaction (IBAN)
		String description11 = "TestDecription1IBAN is this, test-test, moo...";
		String description22 = "TestDecription2IBAN is this, test-test, moo...";
		double amount2 = 50;
		assertNotNull(DB.createTransaction(TransBy.IBAN, account1.getId(), account2.getIban(), description11, description22, -amount2, currency));
		//Assertion (Note: First transaction should always be the most recent.)
		ArrayList<Transaction> transactions11 = DB.getTransactions(account1.getId());
		
		assertFalse(transactions11.isEmpty());
		assertEquals(transactions11.get(0).getAccountId(), account1.getId());
		assertEquals(transactions11.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount2));
		assertEquals(transactions11.get(0).getDescription(), description11);
		ArrayList<Transaction> transactions22 = DB.getTransactions(account2.getId());
		
		assertFalse(transactions22.isEmpty());
		assertEquals(transactions22.get(0).getAccountId(), account2.getId());
		assertEquals(transactions22.get(0).getAmount(), new DecimalFormat("#0.00").format(amount2));
		assertEquals(transactions22.get(0).getDescription(), description22);
		
		//Check Balance
		assertEquals(50.0, Double.parseDouble(DB.getAccountByNumber(number1).getBalance().replace(',', '.')), 0.001);
		assertEquals(150.0, Double.parseDouble(DB.getAccountByNumber(number2).getBalance().replace(',', '.')), 0.001);
		
		//Create transaction (NUMBER)
		String description111 = "TestDecription1NUMBER is this, test-test, moo...";
		String description222 = "TestDecription2NUMBER is this, test-test, moo...";
		double amount3 = 25;
		assertNotNull(DB.createTransaction(TransBy.NUMBER, account1.getId(), account2.getNumber(), description111, description222, -amount3, currency));
		//Assertion (Note: First transaction should always be the most recent.)
		ArrayList<Transaction> transactions111 = DB.getTransactions(account1.getId());
		assertFalse(transactions111.isEmpty());
		assertEquals(transactions111.get(0).getAccountId(), account1.getId());
		assertEquals(transactions111.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount3));
		assertEquals(transactions111.get(0).getDescription(), description111);
		ArrayList<Transaction> transactions222 = DB.getTransactions(account2.getId());
		assertFalse(transactions222.isEmpty());
		assertEquals(transactions222.get(0).getAccountId(), account2.getId());
		assertEquals(transactions222.get(0).getAmount(), new DecimalFormat("#0.00").format(amount3));
		assertEquals(transactions222.get(0).getDescription(), description222);
		
		//Check Balance
		assertEquals(25.0, Double.parseDouble(DB.getAccountByNumber(number1).getBalance().replace(',', '.')), 0.001);
		assertEquals(175.0, Double.parseDouble(DB.getAccountByNumber(number2).getBalance().replace(',', '.')), 0.001);
		
		//Create Transaction (NUMBER) to unknown receiver.
		String description1111 = "TestDecription1NUMBERunknown is this, test-test, moo...";
		String description2222 = "TestDecription2NUMBERunknown is this, test-test, moo...";
		double amount4 = 25;
		assertNotNull(DB.createTransaction(TransBy.NUMBER, account1.getId(), "654456000", description1111, description2222, -amount4, currency));
		//Assertion (Note: First transaction should always be the most recent.)
		
		ArrayList<Transaction> transactions1111 = DB.getTransactions(account1.getId());
		assertFalse(transactions1111.isEmpty());
		assertEquals(transactions1111.get(0).getAccountId(), account1.getId());
		assertEquals(transactions1111.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount4));
		assertEquals(transactions1111.get(0).getDescription(), description1111);
		ArrayList<Transaction> transactions2222 = DB.getTransactions(account2.getId());
		assertFalse(transactions2222.isEmpty());
		assertNotEquals(transactions2222.get(0).getDescription(), description2222);
		
		//Check Balance
		assertEquals(0.0, Double.parseDouble(DB.getAccountByNumber(number1).getBalance().replace(',', '.')), 0.001);
		assertEquals(175.0, Double.parseDouble(DB.getAccountByNumber(number2).getBalance().replace(',', '.')), 0.001);
		
		//Insufficient Funds Exception
		//Attempt to create transaction.
		String description11111 = "TestDecription1TooMuch is this, test-test, moo...";
		String description22222 = "TestDecription2TooMuch is this, test-test, moo...";
		double amountTooMuch = 1000;
		try {
			DB.createTransaction(TransBy.ID, account1.getId(), ""+account2.getId(), description11111, description22222, -amountTooMuch, currency);
			fail("Exception for Insufficient Funds should have been thrown.");
		} catch (DatabaseException e){
			assertTrue(e.getMessage().contains("Insufficients funds."));
			assertEquals(-438, e.getErrorCode());
			assertEquals("08008", e.getSQLState());
		}
		//Assertion
		ArrayList<Transaction> transactionsTooMuch1 = DB.getTransactions(account1.getId());
		assertFalse(transactionsTooMuch1.isEmpty());
		assertNotEquals(transactionsTooMuch1.get(0).getAmount(), new DecimalFormat("#0.00").format(-amountTooMuch));
		assertNotEquals(transactionsTooMuch1.get(0).getDescription(), description11111);
		ArrayList<Transaction> transactionsTooMuch2 = DB.getTransactions(account2.getId());
		assertFalse(transactionsTooMuch2.isEmpty());
		assertNotEquals(transactionsTooMuch2.get(0).getAmount(), new DecimalFormat("#0.00").format(amountTooMuch));
		assertNotEquals(transactionsTooMuch2.get(0).getDescription(), description22222);
		
		//Check Balance
		assertEquals(0.0, Double.parseDouble(DB.getAccountByNumber(number1).getBalance().replace(',', '.')), 0.001);
		assertEquals(175.0, Double.parseDouble(DB.getAccountByNumber(number2).getBalance().replace(',', '.')), 0.001);
	}
	
	@Test
	public void testGetArchive() throws DatabaseException {
		//Test Get Archive
		//Create user
		String username = "TestGetArchive1";
		String cpr = "TestGAR11234";
		String userName = "GAR1 Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
		User user1 = DB.getUserByCpr(cpr);
		assertNotNull(user1);
		
		//Create User
		String username2 = "TestGetArchive2";
		String cpr2 = "TestGAR21234";
		String userName2 = "GAR2 Test Testy Test";
		String institute2 = "Test That Institute";
		String consultant2 = "";
		assertNotNull(DB.createUser(username2, cpr2, userName2, institute2, consultant2));
		User user2 = DB.getUserByCpr(cpr2);
		assertNotNull(user2);
		
		//Create Account
		int userId = user1.getId();
		String accountName = "TestAccount1IsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestGAR1123456789"; 
		String iban = "TestGAR1123456IBAN";
		String currency = "EUR"; 
		double interest = 0.1;
		double balance = 175;
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest));
		Account account1 = DB.getAccountByNumber(number);
		assertNotNull(account1);
		
		//Create Account
		int userId2 = user2.getId();
		String accountName2 = "TestAccount2IsTest";
		String type2 = "TestTypeForTestAccount";
		String number2 = "TestGAR2123456789"; 
		String iban2 = "TestGAR2123456IBAN";
		String currency2 = "EUR"; 
		double interest2 = 0.05;
		assertNotNull(DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2));
		Account account2 = DB.getAccountByNumber(number2);
		assertNotNull(account2);
		
		//Assert there are no transactions
		assertTrue(DB.getTransactions(account1.getId()).isEmpty());
		assertTrue(DB.getTransactions(account2.getId()).isEmpty());
		
		//Deposit to increase balance for further testing.
		assertTrue(DB.createTransaction(account1.getId(), "TestDeposit", balance, currency));
		
		//Create transaction (ID)
		String description1 = "TestDecription1ID is this, test-test, moo...";
		String description2 = "TestDecription2ID is this, test-test, moo...";
		double amount = 100;
		assertNotNull(DB.createTransaction(TransBy.ID, account1.getId(), ""+account2.getId(), description1, description2, -amount, currency));
		//Assertion
		ArrayList<Transaction> transactions1 = DB.getTransactions(account1.getId());
		assertFalse(transactions1.isEmpty());
		assertEquals(transactions1.get(0).getAccountId(), account1.getId());
		assertEquals(transactions1.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount));
		assertEquals(transactions1.get(0).getDescription(), description1);
		ArrayList<Transaction> transactions2 = DB.getTransactions(account2.getId());
		assertFalse(transactions2.isEmpty());
		assertEquals(transactions2.get(0).getAccountId(), account2.getId());
		assertEquals(transactions2.get(0).getAmount(), new DecimalFormat("#0.00").format(amount));
		assertEquals(transactions2.get(0).getDescription(), description2);
		
		//Archive transactions
		DB.archiveTransactions();
		
		//Get Archive
		@SuppressWarnings("deprecation")
		ArrayList<Transaction> archiveOld = DB.getArchive(account1.getId());
		assertNotNull(archiveOld);
		assertFalse(archiveOld.isEmpty());
		
		//Get Archive
		long from = System.currentTimeMillis()-86400000;
		long to = System.currentTimeMillis()+86400000;
		ArrayList<Transaction> archive = DB.searchArchive(user1.getId(), from, to);
		assertNotNull(archive);
		assertFalse(archive.isEmpty());
		assertEquals(transactions1.get(0).getAccountId(), archive.get(0).getAccountId());
		assertEquals(transactions1.get(0).getAmount(), archive.get(0).getAmount());
		assertEquals(transactions1.get(0).getDate(), archive.get(0).getDate());
		assertNotNull(archive.get(0).getCurrency());
		assertEquals(user1.getId(), archive.get(0).getUserId());
	}
	
	@Test
	public void testGetMessages() throws DatabaseException{
		//Test Get Messages.
		//Create user
		String username = "TestGetMessage";
		String cpr = "TestGMM1234";
		String userName = "GMM Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
		assertNotNull(user);
		//Create Account
		int userId = user.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestGMM12"; 
		String iban = "Test123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		//Create Account
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest));
		Account account = DB.getAccountByNumber(number);
		//Get Empty Messages list
		assertTrue(DB.getMessages(user.getId()).isEmpty());
		//Create Message by ID
		String messageid = "TestyForYou ID!";
		assertTrue(DB.createMessage(messageid, account.getId(), ""+account.getId(), TransBy.ID));
		//Get Messages
		ArrayList<Message> messages = DB.getMessages(user.getId());
		//Assertion
		assertFalse(messages.isEmpty());
		assertEquals(messages.get(0).getText(), messageid);
		assertEquals(messages.get(0).getSenderName(), user.getName());
	}
	
	@Test
	public void testGetCurrencies() throws DatabaseException{
		//Test Get Currencies
		//Get Currencies
		ArrayList<String> currencies = DB.getCurrencies();
		assertFalse(currencies.isEmpty());
		assertTrue(currencies.contains("EUR"));
		assertTrue(currencies.contains("DKK"));
		assertTrue(currencies.contains("NOK"));
	}
	
	@Test
	public void testCreateMessage() throws DatabaseException{
		//Test Create and Get Messages.
		//Create user
		String username = "TestCreateGetMessage";
		String cpr = "TestCGM1234";
		String userName = "CGM Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
		assertNotNull(user);
		//Create Account
		int userId = user.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestCGM12"; 
		String iban = "Test123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		//Create Account
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest));
		Account account = DB.getAccountByNumber(number);
		//Get Empty Messages list
		assertTrue(DB.getMessages(user.getId()).isEmpty());
		//Create Message by ID
		String messageid = "TestyForYou ID!";
		assertTrue(DB.createMessage(messageid, account.getId(), ""+account.getId(), TransBy.ID));
		//Assertion
		ArrayList<Message> messages = DB.getMessages(user.getId());
		assertFalse(messages.isEmpty());
		assertEquals(messages.get(0).getText(), messageid);
		assertEquals(messages.get(0).getSenderName(), user.getName());
		
		//Create Message by IBAN
		String messageiban = "TestyForYou IBAN!";
		assertTrue(DB.createMessage(messageiban, account.getId(), ""+account.getIban(), TransBy.IBAN));
		//Assertion
		messages = DB.getMessages(user.getId());
		assertFalse(messages.isEmpty());
		assertEquals(messages.get(0).getText(), messageiban);
		assertEquals(messages.get(0).getSenderName(), user.getName());

		//Create Message by NUMBER
		String messagenumber = "TestyForYou NUMBER!";
		assertTrue(DB.createMessage(messagenumber, account.getId(), ""+account.getNumber(), TransBy.NUMBER));
		//Assertion
		messages = DB.getMessages(user.getId());
		assertFalse(messages.isEmpty());
		assertEquals(messages.get(0).getText(), messagenumber);
		assertEquals(messages.get(0).getSenderName(), user.getName());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCreateDeposit() throws DatabaseException{
		//Test Create Deposit
		//Create user
		String username = "TestCreateDeposit";
		String cpr = "TestCD1234";
		String userName = "CD Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
		User user1 = DB.getUserByCpr(cpr);
		assertNotNull(user1);
		
		//Create Account
		int userId = user1.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestCD123456789"; 
		String iban = "TestCD123456IBAN";
		String currency = "EUR"; 
		double interest = 0.1;
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest));
		Account account1 = DB.getAccountByNumber(number);
		assertNotNull(account1);
		
		//Assert there are no transactions
		assertTrue(DB.getTransactions(account1.getId()).isEmpty());
		
		//Create transaction (ID)
		String description1 = "TestDecription is this, test-test, moo...";
		double amount = 100;
		assertNotNull(DB.createDeposit(account1.getId(), description1, amount));
		//Assertion
		ArrayList<Transaction> transactions1 = DB.getTransactions(account1.getId());
		assertFalse(transactions1.isEmpty());
		assertEquals(transactions1.get(0).getAccountId(), account1.getId());
		assertEquals(transactions1.get(0).getAmount(), new DecimalFormat("#0.00").format(amount));
		assertEquals(transactions1.get(0).getDescription(), description1);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCreateWithdrawal() throws DatabaseException{
		//Test Create Withdrawal
		//Create User
		String username = "TestCreateWithdrawal";
		String cpr = "TestCW1234";
		String userName = "CW Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
		assertNotNull(user);
		
		//Create Account
		int userId = user.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestCW123456789"; 
		String iban = "TestCW123456IBAN";
		String currency = "EUR"; 
		double interest = 0.1;
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest));
		Account account = DB.getAccountByNumber(number);
		assertNotNull(account);
		
		//Assert there are no transactions
		assertTrue(DB.getTransactions(account.getId()).isEmpty());
		
		//Create transaction (ID)
		String description1 = "TestDecription1ID is this, test-test, moo...";
		double amount = 100;
		assertNotNull(DB.createWithdrawal(account.getId(), description1, amount));
		//Assertion
		ArrayList<Transaction> transactions1 = DB.getTransactions(account.getId());
		assertFalse(transactions1.isEmpty());
		assertEquals(transactions1.get(0).getAccountId(), account.getId());
		assertEquals(transactions1.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount));
		assertEquals(transactions1.get(0).getDescription(), description1);
	}
	
	@Test
	public void testSearchUsers() throws SQLException {
		//Create User
		String username1 = "TestSearch1";
		String cpr1 = "TestS1123";
		String userName1 = "TestMoo Testy Test";
		String institute1 = "Test That Institute";
		String consultant1 = "";
		User user1 = DB.createUser(username1, cpr1, userName1, institute1, consultant1);
		assertNotNull(user1);
		
		//Create User
		String username2 = "TestSearch2";
		String cpr2 = "TestS2123";
		String userName2 = "Test TestyMoo Test";
		String institute2 = "Test That Institute";
		String consultant2 = "";
		User user2 = DB.createUser(username2, cpr2, userName2, institute2, consultant2);
		assertNotNull(user2);

		//Create User
		String username3 = "TestSearch3";
		String cpr3 = "TestS3123";
		String userName3 = "Test Testy TestMoo";
		String institute3 = "Test That Institute";
		String consultant3 = "";
		User user3 = DB.createUser(username3, cpr3, userName3, institute3, consultant3);
		assertNotNull(user3);
		
		//Search by Name
		String byName = "TestMoo";
		ArrayList<User> listByName = DB.searchUsers(byName);
		assertNotNull(listByName);
		//Assertion
		assertFalse(listByName.isEmpty());
		assertEquals(2, listByName.size());
		assertEquals(user1.getName(), listByName.get(0).getName());
		assertEquals(user3.getName(), listByName.get(1).getName());
		
		//Search by CPR
		String byCPR = "S2123";
		ArrayList<User> listByCPR = DB.searchUsers(byCPR);
		assertNotNull(listByCPR);
		//Assertion
		assertFalse(listByCPR.isEmpty());
		assertEquals(1, listByCPR.size());
		assertEquals(user2.getName(), listByCPR.get(0).getName());
	}
	
	@Test
	public void testCurrencyConversion() throws SQLException {
		//Create User
		String username = "TestCurrencyConversion";
		String cpr = "TestCC123";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);

		//Create Account
		int userId1 = user.getId();
		String accountName1 = "TestAccountIsTest";
		String type1 = "TestTypeForTestAccount";
		String number1 = "TestCC112";
		String iban1 = "TestCC112IBAN";
		String currency1 = "EUR"; 
		double interest1 = 0.05;
		Account account1 = DB.createAccount(userId1, accountName1, type1, number1, iban1, currency1, interest1);
		assertNotNull(account1);
		
		//Create Account
		int userId2 = user.getId();
		String accountName2 = "TestAccountIsTest";
		String type2 = "TestTypeForTestAccount";
		String number2 = "TestCC212";
		String iban2 = "TestCC212IBAN";
		String currency2 = "DKK";
		double interest2 = 0.05;
		Account account2 = DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2);
		assertNotNull(account2);
		
		//Alternate Get Currency
		assertEquals("EUR" ,DB.getReceiverCurrency(""+account1.getId(), TransBy.ID));
		assertEquals("DKK" ,DB.getReceiverCurrency(account2.getIban(), TransBy.IBAN));
		assertEquals("EUR" ,DB.getReceiverCurrency(account1.getNumber(), TransBy.NUMBER));
		
		//Deposit to increase balance for further testing.
		double depositamount = 1000;
		assertTrue(DB.createTransaction(account1.getId(), "TestDeposit", depositamount, currency1));
		assertEquals(1000.0, Double.parseDouble(DB.getAccount(account1.getId()).getBalance().replace(',', '.')), 0.001);
		
		//Create transaction
		String description1 = "TestDecription1Currency is this, test-test, moo...";
		String description2 = "TestDecription2Currency is this, test-test, moo...";
		double amount = 1000;
		String currency = "EUR";
		assertNotNull(DB.createTransaction(TransBy.ID, account1.getId(), ""+account2.getId(), description1, description2, -amount, currency));
		//Assertion
		ArrayList<Transaction> transactions1 = DB.getTransactions(account1.getId());
		assertFalse(transactions1.isEmpty());
		assertEquals(transactions1.get(0).getAccountId(), account1.getId());
		assertEquals(transactions1.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount));
		assertEquals(transactions1.get(0).getDescription(), description1);
		ArrayList<Transaction> transactions2 = DB.getTransactions(account2.getId());
		assertFalse(transactions2.isEmpty());
		assertEquals(transactions2.get(0).getAccountId(), account2.getId());
		assertEquals(transactions2.get(0).getDescription(), description2);
		
		//Conversion is imprecise because yahoo...
		//Check Balance
		assertEquals(0.0, Double.parseDouble(DB.getAccountByNumber(number1).getBalance().replace(',', '.')), 0.001);
		assertEquals(7400.0, Double.parseDouble(DB.getAccountByNumber(number2).getBalance().replace(',', '.')), 100);
	}
	
	@Test
	public void testCloseAccount() throws SQLException {
		//Create User
		String username = "TestCloseAccount";
		String cpr = "TestCCA12";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		User user = DB.createUser(username, cpr, userName, institute, consultant);
		assertNotNull(user);

		//Create Account
		int userId1 = user.getId();
		String accountName1 = "TestAccountIsTest";
		String type1 = "TestTypeForTestAccount";
		String number1 = "TestCCA112";
		String iban1 = "TestCCA112IBAN";
		String currency1 = "EUR"; 
		double interest1 = 0.05;
		Account account1 = DB.createAccount(userId1, accountName1, type1, number1, iban1, currency1, interest1);
		assertNotNull(account1);
		
		//Create Account
		int userId2 = user.getId();
		String accountName2 = "TestAccountIsTest";
		String type2 = "TestTypeForTestAccount";
		String number2 = "TestCCA212";
		String iban2 = "TestCCA212IBAN";
		String currency2 = "EUR";
		double interest2 = 0.05;
		Account account2 = DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2);
		assertNotNull(account2);

		//Do Deposit for further testing
		int accountId = account1.getId();
		String description = "TestDeposit";
		double amount = 100;
		assertTrue(DB.createTransaction(accountId, description, amount, currency1));
		assertEquals(100.0, Double.parseDouble(DB.getAccount(account1.getId()).getBalance().replace(',', '.')), 0.001);
		
		//Assert that Account exists
		assertNotNull(DB.getAccount(account1.getId()));
		
		//Delete Account with higher than 0 balance.
		String receiverDescription = "TestDescription Closing Account";
		DB.deleteAccountWithTransfer(account1.getId(), account2.getId(), receiverDescription, amount, currency1);
		
		//Assert Account is closed
		assertNull(DB.getAccount(account1.getId()));
		
		//Assert balance on account 2
		assertEquals(100.0, Double.parseDouble(DB.getAccount(account2.getId()).getBalance().replace(',', '.')), 0.001);
	}
}
