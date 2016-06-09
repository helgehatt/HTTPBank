package ibm.test;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.InputException;
import ibm.resource.Transaction;
import ibm.resource.User;
//hej
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
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertTrue(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account account = DB.getAccountByNumber(number);
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
		assertTrue(DB.createTransaction(accountId, description, amount));
		ArrayList<Transaction> transaction = DB.getTransactions(accountId);
		//Assertion
		assertEquals(transaction.get(0).getAccountId(), accountId);
		assertEquals(transaction.get(0).getAmount(), new DecimalFormat("#0.00").format(amount));
		assertEquals(transaction.get(0).getDateAsString(), new SimpleDateFormat("yyyy-MM-dd").format(date));
		assertEquals(transaction.get(0).getDescription(), description);
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
		assertTrue(DB.createUser(username, password, cpr, name, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertTrue(DB.createUser(username, password, cpr, name, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
	
	@Test
	public void testGetUsers() throws SQLException{
		//Test Get Users
		String username = "TestGetUsers";
		String password = "Test1234";
		String cpr = "TestGM1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User newUser = DB.getUserByCpr(cpr);
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
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertTrue(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account account = DB.getAccountByNumber(number);
		assertNotNull(account);
		
		//Create Transaction
		int accountId = account.getId();
		String description = "TestDecription is this, test-test, moo..."; 
		double amount = 100;
		//Create Transaction
		assertTrue(DB.createTransaction(accountId, description, amount));
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
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertTrue(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account newAccount = DB.getAccountByNumber(number);
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
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertTrue(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account newAccount = DB.getAccountByNumber(number);
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
		assertEquals(new DecimalFormat("#0.00").format(newBalance), updatedAccount.getBalance());
		assertEquals(currency, updatedAccount.getCurrency());
		assertEquals(newIban, updatedAccount.getIban());
		assertEquals(new DecimalFormat("#0.00").format(newInterest)+"%", updatedAccount.getInterest());
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
		assertEquals(new DecimalFormat("#0.00").format(newNewBalance), updatedUpdatedAccount.getBalance());
		assertEquals(newNewCurrency, updatedUpdatedAccount.getCurrency());
		assertEquals(newNewIban, updatedUpdatedAccount.getIban());
		assertEquals(new DecimalFormat("#0.00").format(newNewInterest)+"%", updatedUpdatedAccount.getInterest());
		assertEquals(newNewAccountName, updatedUpdatedAccount.getName());
		assertEquals(newNewNumber, updatedUpdatedAccount.getNumber());
	}

	@Test
	public void testUpdateUser() throws SQLException {
		//Test Update User
		//Create User
		String username = "TestUpdateUser";
		String password = "Test1234";
		String cpr = "TestUU1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertTrue(DB.updateUser(user.getId(), newusername, USER.USERNAME));
		assertTrue(DB.updateUser(user.getId(), newuserName, USER.NAME));
		assertTrue(DB.updateUser(user.getId(), newpassword, USER.PASSWORD));
		assertTrue(DB.updateUser(user.getId(), newconsultant, USER.CONSULTANT));
		assertTrue(DB.updateUser(user.getId(), newcpr, USER.CPR));
		assertTrue(DB.updateUser(user.getId(), newinstitute, USER.INSTITUTE));
		
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
		String newnewpassword = "TestNN1234";
		String newnewcpr = "TestUUNN1234";
		String newnewuserName = "New Moo Testy Test";
		String newnewinstitute = "New Moo That Institute";
		String newnewconsultant = "Tommy the 2rd";
		assertTrue(DB.updateUser(user.getId(), newnewusername, newnewpassword, newnewcpr, newnewuserName, newnewinstitute, newnewconsultant));
		//Assertion
		User updatedUpdatedUser = DB.getUser(user.getId());
		assertEquals(user.getId(), updatedUpdatedUser.getId());
		assertEquals(newnewconsultant, updatedUpdatedUser.getConsultant());
		assertEquals(newnewcpr, updatedUpdatedUser.getCpr());
		assertEquals(newnewinstitute, updatedUpdatedUser.getInstitute());
		assertEquals(newnewuserName, updatedUpdatedUser.getName());
		assertEquals(newnewusername, updatedUpdatedUser.getUsername());
		assertFalse(0 < DB.checkLogin(newusername, newpassword));
		assertFalse(0 < DB.checkLogin(newnewusername, newpassword));
		assertFalse(0 < DB.checkLogin(newusername, newnewpassword));
		assertTrue(0 < DB.checkLogin(newnewusername, newnewpassword));
	}
	
	@Test
	public void testDeleteMethods() throws SQLException {
		//Test Delete User
		String username = "TestDeleteUserByCpr";
		String password = "Test1234";
		String cpr = "TestDUBC1234";
		String userName = "DUBC Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		assertTrue(DB.createUser(username, password, cpr, userName, institute, consultant));
		User user = DB.getUserByCpr(cpr);
		assertNotNull(user);
		
		//Create Account
		int userId = user.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "TestDABN123456789"; 
		String iban = "TestDABN123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Create Account
		assertTrue(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account account = DB.getAccountByNumber(number);
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
		String password2 = "Test1234";
		String cpr2 = "TestDU1234";
		String userName2 = "DU Test Testy Test";
		String institute2 = "Test That Institute";
		String consultant2 = "";
		//Create user
		assertTrue(DB.createUser(username2, password2, cpr2, userName2, institute2, consultant2));
		User user2 = DB.getUserByCpr(cpr2);
		assertNotNull(user2);
		
		//Create Account
		int userId2 = user2.getId();
		String accountName2 = "TestAccountIsTest";
		String type2 = "TestTypeForTestAccount";
		String number2 = "TestDA123456789"; 
		String iban2 = "TestDA123456IBAN";
		String currency2 = "EUR"; 
		double interest2 = 0.05;
		double balance2 = 0;
		//Create Account
		assertTrue(DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2, balance2));
		Account account2 = DB.getAccountByNumber(number2);
		assertNotNull(account2);
		
		//Delete Account
		assertTrue(DB.deleteAccount(account2.getId()));
		assertNull(DB.getAccount(account2.getId()));
		assertNull(DB.getAccountByNumber(number2));
		
		//Delete User
		User sameUser2 = DB.getUserByCpr(cpr2);
		assertNotNull(sameUser2);
		assertTrue(DB.deleteUser(sameUser2.getUsername()));
		assertNull(DB.getUser(sameUser2.getId()));
		assertNull(DB.getUserByCpr(cpr2));
	}
}
