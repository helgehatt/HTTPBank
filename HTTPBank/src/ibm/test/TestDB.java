package ibm.test;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.InputException;
import ibm.resource.Transaction;
import ibm.resource.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDB {
	
	private static Connection connection;
	
	@BeforeClass
	public static void getConnection() throws SQLException {
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
		connection = DriverManager.getConnection("jdbc:db2://192.86.32.54:5040/DALLASB", properties);
	}
	
	@AfterClass
	public static void cleanUp() throws SQLException{
		//Delete everything!
		Statement updateBalance = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		updateBalance.executeUpdate("UPDATE DTUGRP07.ACCOUNTS "
				+ "SET BALANCE = '0' "
				+ "WHERE NAME LIKE 'Test%';");
		updateBalance.close();
		Statement deleteAccount = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		deleteAccount.executeUpdate("DELETE FROM DTUGRP07.ACCOUNTS "
				+ "WHERE NAME LIKE 'Test%';");
		deleteAccount.close();
		Statement deleteUser = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		deleteUser.executeUpdate("DELETE FROM DTUGRP07.USERS "
				+ "WHERE USERNAME LIKE 'Test%';");
		deleteUser.close();
		
		//Close connection.
		connection.close();
	}
	
	@Test
	public void testCreateMethods() throws SQLException {
		//Test Create User
		String username = "TestCreateUser";
		String password = "Test1234";
		String cpr = "TestCU1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Attempt to get a user that doesn't yet exist.
		User nullUser = DB.getUserByCpr(cpr);
		assertNull(nullUser);
		//Create user
		User user = DB.createUser(username, password, cpr, userName, institute, consultant);
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
		String number = "Test123456789"; 
		String iban = "Test123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Get not existing account
		Account nullAccount = DB.getAccountByNumber(number);
		assertNull(nullAccount);
		//Create Account
		Account account = DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance);
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
		Transaction transaction = DB.createTransaction(accountId, description, amount);
		//Get newly created transaction from list
		Transaction sameTransaction = DB.getTransactions(accountId).get(0);
		//Assertion
		assertEquals(transaction.getAccountId(), sameTransaction.getAccountId());
		assertEquals(transaction.getAmount(), sameTransaction.getAmount());
		assertEquals(transaction.getDateAsString(), sameTransaction.getDateAsString());
		assertEquals(transaction.getDescription(), sameTransaction.getDescription());
	}
	
	@Test
	public void testCheckLogin() throws SQLException, InputException {
		//Test Check Login
		String username = "TestCreateLogin";
		String password = "Test1234";
		String cpr = "TestCL1234";
		String name = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Attempt to get a user that doesn't yet exist.
		User nullUser = DB.getUserByCpr(cpr);
		assertNull(nullUser);
		//Create user
		User user = DB.createUser(username, password, cpr, name, institute, consultant);
		//Check login
		int id = DB.checkLogin(username, password);
		//Assert Id is equal
		assertEquals(user.getId(), id);
	}
	
	@Test
	public void testGetUser() throws SQLException{
		//Test Get User
		String username = "TestGetUser";
		String password = "Test1234";
		String cpr = "TestGS1234";
		String name = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create a user to get
		User user = DB.createUser(username, password, cpr, name, institute, consultant);
		//Get user
		User sameUser = DB.getUser(user.getId());
		//Assert
		assertEquals(user.getId(), sameUser.getId());
		assertEquals(user.getConsultant(), sameUser.getConsultant());
		assertEquals(user.getCpr(), sameUser.getCpr());
		assertEquals(user.getInstitute(), sameUser.getInstitute());
		assertEquals(user.getName(), sameUser.getName());
		assertEquals(user.getUsername(), sameUser.getUsername());
	}
	
	public void testGetUsers() throws SQLException{
		//Test Get Users
		String username = "TestGetUsers";
		String password = "Test1234";
		String cpr = "TestGM1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		User newUser = DB.createUser(username, password, cpr, userName, institute, consultant);
		assertNotNull(newUser);
		//Get Users
		ArrayList<User> users = DB.getUsers();
		assertFalse(users.isEmpty());
		//Assertion
		for (User user : users){
			assertNotNull(user.getId());
			assertNotNull(user.getConsultant());
			assertNotNull(user.getCpr());
			assertNotNull(user.getInstitute());
			assertNotNull(user.getName());
			assertNotNull(user.getUsername());
		}
	}

	@Test
	public void testGetTransactions() throws SQLException{
		//Test Get Transactions
		//Create User
		String username = "TestGetTrans";
		String password = "Test1234";
		String cpr = "TestGT1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		User user = DB.createUser(username, password, cpr, userName, institute, consultant);
		assertNotNull(user);
		
		//Create Account
		int userId = user.getId();
		String accountName = "TestGetTrans";
		String type = "TestTypeForTestAccount";
		String number = "TestGT123456789"; 
		String iban = "TestGT123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Create Account
		Account account = DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance);
		assertNotNull(account);
		
		//Create Transaction
		int accountId = account.getId();
		String description = "TestDecription is this, test-test, moo..."; 
		double amount = 100;
		//Create Transaction
		Transaction newTransaction = DB.createTransaction(accountId, description, amount);
		assertNotNull(newTransaction);
		//Get Transactions
		ArrayList<Transaction> transactions = DB.getTransactions(accountId);
		assertFalse(transactions.isEmpty());
		//Assertion
		for (Transaction transaction : transactions){
			assertNotNull(transaction.getId());
			assertNotNull(transaction.getAccountId());
			assertNotNull(transaction.getAmount());
			assertNotNull(transaction.getDate());
			assertNotNull(transaction.getDateRaw());
			assertNotNull(transaction.getDateAsString());
			assertNotNull(transaction.getDescription());
		}
	}

	@Test
	public void testGetAccounts() throws SQLException{
		//Test Get Accounts
		//Create User
		String username = "TestGetAccount";
		String password = "Test1234";
		String cpr = "TestGA1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		User user = DB.createUser(username, password, cpr, userName, institute, consultant);
		assertNotNull(user);
		
		//Create Account
		int userId = user.getId();
		String accountName = "TestGetAccount";
		String type = "TestTypeForTestAccount";
		String number = "TestGA123456789"; 
		String iban = "TestGA123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Create Account
		Account newAccount = DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance);
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
			assertTrue(0.0 == account.getBalanceRaw());
			assertTrue(0.0 == Double.parseDouble(account.getBalance()));
		}
	}
	
	public void testUpdateAccount() throws SQLException {
		//Test Update Account
		//Create User
		String username = "TestUpdateAccount";
		String password = "Test1234";
		String cpr = "TestUA1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		User user = DB.createUser(username, password, cpr, userName, institute, consultant);
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
		Account newAccount = DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance);
		assertNotNull(newAccount);
		int accountId = newAccount.getId();
		//Assert original values.
		assertEquals(userId, newAccount.getUserId());
		assertTrue(balance == newAccount.getBalanceRaw());
		assertEquals(currency, newAccount.getCurrency());
		assertEquals(iban, newAccount.getIban());
		assertEquals(interest, newAccount.getInterest());
		assertEquals(accountName, newAccount.getName());
		assertEquals(number, newAccount.getNumber());
		//Update Account 
		String newAccountName = "TestUpdateAccountNew";
		assertTrue(DB.updateAccount(accountId, newAccountName, DB.ACCOUNT.NAME));
		double newBalance = 100;
		assertTrue(DB.updateAccount(accountId, ""+newBalance, DB.ACCOUNT.BALANCE));
		String newIban = "TestUAN123456IBAN";
		assertTrue(DB.updateAccount(accountId, newIban, DB.ACCOUNT.IBAN));
		double newInterest = 0.10;
		assertTrue(DB.updateAccount(accountId, ""+newInterest, DB.ACCOUNT.INTEREST));
		String newNumber = "TestUAN123456789";
		assertTrue(DB.updateAccount(accountId, newNumber, DB.ACCOUNT.NUMBER));
		String newType = "TestNewTypeForTestAccount";
		assertTrue(DB.updateAccount(accountId, newType, DB.ACCOUNT.TYPE));
		//Assertion
		Account updatedAccount = DB.getAccount(accountId);
		assertEquals(userId, updatedAccount.getUserId());
		assertTrue(newBalance == updatedAccount.getBalanceRaw());
		assertEquals(currency, updatedAccount.getCurrency());
		assertEquals(newIban, updatedAccount.getIban());
		assertEquals(newInterest, updatedAccount.getInterest());
		assertEquals(newAccountName, updatedAccount.getName());
		assertEquals(newNumber, updatedAccount.getNumber());
		
		//Update Account 
		String newNewAccountName = "TestUpdateAccountNew";
		double newNewBalance = 100;
		String newNewIban = "TestUAN123456IBAN";
		double newNewInterest = 0.10;
		String newNewNumber = "TestUAN123456789";
		String newNewType = "TestNewTypeForTestAccount";
		String newNewCurrency = "DKK";
		assertTrue(DB.updateAccount(accountId, newNewAccountName, newNewType, newNewNumber, newNewIban, newNewCurrency, newNewInterest, newNewBalance));
		//Assertion
		Account updatedUpdatedAccount = DB.getAccount(accountId);
		assertEquals(userId, updatedUpdatedAccount.getUserId());
		assertTrue(newNewBalance == updatedUpdatedAccount.getBalanceRaw());
		assertEquals(newNewCurrency, updatedUpdatedAccount.getCurrency());
		assertEquals(newNewIban, updatedUpdatedAccount.getIban());
		assertEquals(newNewInterest, updatedUpdatedAccount.getInterest());
		assertEquals(newNewAccountName, updatedUpdatedAccount.getName());
		assertEquals(newNewNumber, updatedUpdatedAccount.getNumber());
	}

	private static void testUpdateUser(int userId, String value, USER attribute) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.updateUser(userId, value, attribute);
		System.out.println("Updated New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}
	
	private static void testDeleteUserByCpr(String cpr) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteUserByCpr(cpr);
		System.out.println("Delete New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testDeleteUser(String userId) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteUser(userId);
		System.out.println("Delete New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testDeleteAccountByNumber(String number) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteAccountByNumber(number);
		System.out.println("Delete New Account: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testDeleteAccount(int accountId) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteAccount(accountId);
		System.out.println("Delete New Account: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}
}
