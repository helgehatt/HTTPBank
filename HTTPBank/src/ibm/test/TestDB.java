package ibm.test;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.db.DB.TransBy;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.InputException;
import ibm.resource.Message;
import ibm.resource.Transaction;
import ibm.resource.User;

import java.io.PrintWriter;
import java.io.StringWriter;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.AfterClass;
import org.junit.Test;
import org.mockito.Mockito;

public class TestDB extends Mockito {
	
	private static Connection connection;
	
	
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
		connection = DriverManager.getConnection("jdbc:db2://192.86.32.54:5040/DALLASB", properties);
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
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
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
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
		assertNotNull(DB.createTransaction(accountId, description, amount));
		ArrayList<Transaction> transaction = DB.getTransactions(accountId);
		//Assertion
		assertEquals(transaction.get(0).getAccountId(), accountId);
		assertEquals(transaction.get(0).getAmount(), new DecimalFormat("#0.00").format(amount));
		assertEquals(transaction.get(0).getDate(), new SimpleDateFormat("dd.MM.yyyy").format(date));
		assertEquals(transaction.get(0).getDescription(), description);
		
		//Archive transactions
		DB.archiveTransactions();
		
		//Get Archive
		ArrayList<Transaction> archive = DB.getArchive(account.getId());
		assertFalse(archive.isEmpty());
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
		assertNotNull(DB.createUser(username, cpr, name, institute, consultant));
		User user = DB.getUserByCpr(cpr);
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
		assertNotNull(DB.createUser(username, cpr, name, institute, consultant));
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
			assertNotNull(DB.getUserByCpr(cpr+i));
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
		
		for (User user : users){
			System.out.println(user.getName()+" : "+user.getCpr());
		}
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
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
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
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account account = DB.getAccountByNumber(number);
		assertNotNull(account);
		
		//Create Transaction
		int accountId = account.getId();
		String description = "TestDecription is this, test-test, moo..."; 
		double amount = 100;
		//Create Transaction
		assertNotNull(DB.createTransaction(accountId, description, amount));
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
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
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
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
	public void testUpdateAccount() throws DatabaseException {
		//Test Update Account
		//Create User
		String username = "TestUpdateAccount";
		String cpr = "TestUA1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Create user
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
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
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
		double newNewBalance = 100;
		String newNewIban = "TestUAN123456IBAN";
		double newNewInterest = 0.10;
		String newNewNumber = "TestUAN123456789";
		String newNewType = "TestNewTypeForTestAccount";
		String newNewCurrency = "DKK";
		assertNotNull(DB.updateAccount(accountId, newNewAccountName, newNewType, newNewNumber, newNewIban, newNewCurrency, newNewInterest, newNewBalance));
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
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
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
		assertNotNull(DB.updateUser(user.getId(), newnewusername, newnewcpr, newnewuserName, newnewinstitute, newnewconsultant));
		//Assertion
		User updatedUpdatedUser = DB.getUser(user.getId());
		assertEquals(user.getId(), updatedUpdatedUser.getId());
		assertEquals(newnewconsultant, updatedUpdatedUser.getConsultant());
		assertEquals(newnewcpr, updatedUpdatedUser.getCpr());
		assertEquals(newnewinstitute, updatedUpdatedUser.getInstitute());
		assertEquals(newnewuserName, updatedUpdatedUser.getName());
		assertEquals(newnewusername, updatedUpdatedUser.getUsername());
		assertFalse(0 < DB.checkLogin(newusername, newpassword));
		assertTrue(0 < DB.checkLogin(newnewusername, newpassword));
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
		assertNotNull(DB.createUser(username, cpr, userName, institute, consultant));
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
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
		String cpr2 = "TestDU1234";
		String userName2 = "DU Test Testy Test";
		String institute2 = "Test That Institute";
		String consultant2 = "";
		assertNotNull(DB.createUser(username2, cpr2, userName2, institute2, consultant2));
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
		assertNotNull(DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2, balance2));
		Account account2 = DB.getAccountByNumber(number2);
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
	public void testCreateTransactionAndGetArchive() throws DatabaseException {
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
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
		double balance2 = 0;
		assertNotNull(DB.createAccount(userId2, accountName2, type2, number2, iban2, currency2, interest2, balance2));
		Account account2 = DB.getAccountByNumber(number2);
		assertNotNull(account2);
		
		//Assert there are no transactions
		assertTrue(DB.getTransactions(account1.getId()).isEmpty());
		assertTrue(DB.getTransactions(account2.getId()).isEmpty());
		
		//Create transaction (ID)
		String description1 = "TestDecription1ID is this, test-test, moo...";
		String description2 = "TestDecription2ID is this, test-test, moo...";
		double amount = 100;
		assertNotNull(DB.createTransaction(TransBy.ID, account1.getId(), ""+account2.getId(), description1, description2, -amount, amount));
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
		
		//Create transaction (IBAN)
		String description11 = "TestDecription1IBAN is this, test-test, moo...";
		String description22 = "TestDecription2IBAN is this, test-test, moo...";
		double amount2 = 50;
		assertNotNull(DB.createTransaction(TransBy.IBAN, account1.getId(), account2.getIban(), description11, description22, -amount2, amount2));
		//Assertion (Note: First transaction should always be the most recent.)
		ArrayList<Transaction> transactions11 = DB.getTransactions(account1.getId());
		
		for (Transaction trans : transactions11)
			System.out.println(trans.getDescription()+" : "+trans.getDateRaw());
		
		assertFalse(transactions11.isEmpty());
		assertEquals(transactions11.get(0).getAccountId(), account1.getId());
		assertEquals(transactions11.get(0).getAmount(), new DecimalFormat("#0.00").format(-amount2));
		assertEquals(transactions11.get(0).getDescription(), description11);
		ArrayList<Transaction> transactions22 = DB.getTransactions(account2.getId());
		
		for (Transaction trans : transactions22)
			System.out.println(trans.getDescription()+" : "+trans.getDateRaw());
		
		assertFalse(transactions22.isEmpty());
		assertEquals(transactions22.get(0).getAccountId(), account2.getId());
		assertEquals(transactions22.get(0).getAmount(), new DecimalFormat("#0.00").format(amount2));
		assertEquals(transactions22.get(0).getDescription(), description22);
		
		//Create transaction (NUMBER)
		String description111 = "TestDecription1NUMBER is this, test-test, moo...";
		String description222 = "TestDecription2NUMBER is this, test-test, moo...";
		double amount3 = 25;
		assertNotNull(DB.createTransaction(TransBy.NUMBER, account1.getId(), account2.getNumber(), description111, description222, -amount3, amount3));
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
		
		//Archive transactions
		DB.archiveTransactions();
		
		//Get Archive
		ArrayList<Transaction> archive = DB.getArchive(account1.getId());
		assertFalse(archive.isEmpty());
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
		String number = "Test123456789"; 
		String iban = "Test123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Create Account
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account account = DB.getAccountByNumber(number);
		//Get Empty Messages list
		assertTrue(DB.getMessages(user.getId()).isEmpty());
		//Create Message by ID
		String messageid = "TestyForYou ID!";
		assertTrue(DB.createMessage(messageid, user.getId(), ""+account.getId(), TransBy.ID));
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
		String number = "Test123456789"; 
		String iban = "Test123456IBAN";
		String currency = "EUR"; 
		double interest = 0.05;
		double balance = 0;
		//Create Account
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
		Account account = DB.getAccountByNumber(number);
		//Get Empty Messages list
		assertTrue(DB.getMessages(user.getId()).isEmpty());
		//Create Message by ID
		String messageid = "TestyForYou ID!";
		assertTrue(DB.createMessage(messageid, user.getId(), ""+account.getId(), TransBy.ID));
		//Assertion
		ArrayList<Message> messages = DB.getMessages(user.getId());
		assertFalse(messages.isEmpty());
		assertEquals(messages.get(0).getText(), messageid);
		assertEquals(messages.get(0).getSenderName(), user.getName());
		
		//Create Message by IBAN
		String messageiban = "TestyForYou IBAN!";
		assertTrue(DB.createMessage(messageiban, user.getId(), ""+account.getIban(), TransBy.IBAN));
		//Assertion
		messages = DB.getMessages(user.getId());
		assertFalse(messages.isEmpty());
		assertEquals(messages.get(0).getText(), messageiban);
		assertEquals(messages.get(0).getSenderName(), user.getName());

		//Create Message by NUMBER
		String messagenumber = "TestyForYou NUMBER!";
		assertTrue(DB.createMessage(messagenumber, user.getId(), ""+account.getNumber(), TransBy.NUMBER));
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
		double balance = 0;
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
		double balance = 0;
		assertNotNull(DB.createAccount(userId, accountName, type, number, iban, currency, interest, balance));
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
	public void testExceptionHandling() throws SQLException {
		//Test Exception Handling
		Connection connection = mock(Connection.class);
		String message = "A mocking exception...";
		int errorCode = 1337;
		String sqlState = "2E000";
		when(connection.prepareStatement(anyString(), anyInt(), anyInt(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareCall(anyString(), anyInt(), anyInt(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		DB.setConnection(connection);
		
		//Instant failure exception.
		try {
			DB.checkLogin("Thomas", "1234");
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(message, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
		
	}
	
	
	public void testLoginServlet() throws Exception {
		String username = "Lenny";
		String password = "check";
		HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = new PrintWriter(new StringWriter());
	    when(request.getParameter("username")).thenReturn(username);
	    when(request.getParameter("password")).thenReturn(password);
	    when(response.getWriter()).thenReturn(writer);
	    
	    new TestServlet().doPost(request, response);
	    verify(request, atLeast(1)).getParameter(username);
	    writer.flush();
	    System.out.println(writer.toString());
	    //change  osdkf
	    //assertTrue(writer.toString().contains(""));
	}
}
