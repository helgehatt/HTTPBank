package ibm.test;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.db.DB.ACCOUNT;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.InputException;
import ibm.resource.Transaction;
import ibm.resource.User;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.Test;

public class TestDB {
	@AfterClass
	private void testCleanUp(){
		//Delete everything!
	}
	
	@Test
	private void testCreateMethods() throws SQLException {
		//Test Create User
		String username = "TestCreateUser";
		String password = "Test1234";
		String cpr = "Test1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		//Attempt to get a user that doesn't yet exist.
		User nullUser = DB.getUserByCpr(cpr);
		assertNull(nullUser);
		//Create user
		User user = DB.createUser(username, password, cpr, userName, institute, consultant);
		//Get the newly created user
		User sameUser = DB.getUserByCpr(cpr);
		//Assertion
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
		assertEquals(transaction.getDate(), sameTransaction.getDate());
		assertEquals(transaction.getDateAsString(), sameTransaction.getDateAsString());
		assertEquals(transaction.getDateRaw(), sameTransaction.getDateRaw());
		assertEquals(transaction.getDescription(), sameTransaction.getDescription());
	}
	
	@Test
	public void testCheckLogin() throws SQLException, InputException {
		//Test Check Login
		String username = "TestCreateLogin";
		String password = "Test1234";
		String cpr = "TestLogin1234";
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
		String cpr = "TestGetSingle1234";
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
	
	@Test
	public static ArrayList<User> testGetUsers() throws SQLException{
		long start = System.currentTimeMillis();
		ArrayList<User> users = DB.getUsers();
		System.out.println("Got Users, Query Time: " + (System.currentTimeMillis()-start));
		for (User user : users)
			System.out.println(user.getId()+" : "+user.getUsername());
		return users;
	}

	@Test
	public static ArrayList<Transaction> testGetTransactions(int id) throws SQLException{
		long start = System.currentTimeMillis();
		ArrayList<Transaction> transactions = DB.getTransactions(id);
		System.out.println("Got Transactions, Query Time: " + (System.currentTimeMillis()-start));
		for (Transaction transaction : transactions)
			printTransaction(transaction);
		return transactions;
	}

	@Test
	public static ArrayList<Account> testGetAccounts(int id) throws SQLException{
		long start = System.currentTimeMillis();
		ArrayList<Account> accounts = DB.getAccounts(id);
		System.out.println("Got Accounts, Query Time: " + (System.currentTimeMillis()-start));
		for (Account account : accounts)
			printAccount(account);
		return accounts;
	}
	
	@Test
	private static void testUpdateAccount(int accountId, String value, ACCOUNT attribute) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.updateAccount(accountId, value, attribute);
		System.out.println("Updated New Account: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	@Test
	private static void testUpdateUser(int userId, String value, USER attribute) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.updateUser(userId, value, attribute);
		System.out.println("Updated New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}
	
	@Test
	private static void testDeleteUserByCpr(String cpr) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteUserByCpr(cpr);
		System.out.println("Delete New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	@Test
	private static void testDeleteUser(String userId) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteUser(userId);
		System.out.println("Delete New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	@Test
	private static void testDeleteAccountByNumber(String number) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteAccountByNumber(number);
		System.out.println("Delete New Account: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	@Test
	private static void testDeleteAccount(int accountId) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.deleteAccount(accountId);
		System.out.println("Delete New Account: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}
	
	public static void printUser(User user){
		System.out.println(user.getId() +" : "+ user.getUsername() +" : "+ user.getCpr() +" : "+ user.getName() +" : "+ user.getInstitute() +" : "+ user.getConsultant());
	}
	
	private static void printAccount(Account account) {
		System.out.println(account.getUserId() +" : "+ account.getId() +" : "+ account.getName() +" : "+ account.getType() +" : "+ account.getNumber() +" : "+ account.getIban() +" : "+ account.getCurrency() +" : "+ account.getInterest() +" : "+account.getBalance());
	}
	
	private static void printTransaction(Transaction transaction) {
		System.out.println(transaction.getAccountId() +" : "+ transaction.getId() +" : "+ transaction.getDateAsString() +" : "+ transaction.getDescription() +" : "+ transaction.getAmount());
	}
}
