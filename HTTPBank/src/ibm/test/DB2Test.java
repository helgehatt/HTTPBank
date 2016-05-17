package ibm.test;

import ibm.db.DB;
import ibm.db.DB.ACCOUNT;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.InputException;
import ibm.resource.Transaction;
import ibm.resource.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DB2Test {
	//Main
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InputException {
		DB.checkConnection();
		testCheckLogin("Thomas", "1234");
		System.out.println();
		testGetUsers();
		System.out.println();
		testGetUser(1);
		System.out.println();
		testGetAccounts(1);
		System.out.println();
		testGetTransactions(1);
		System.out.println();
		
		int userId = testCreateUser("NewUser", "user", "123456-7890", "User", "NewInstitute", null).getId();
		System.out.println();
		int accountId = testCreateAccount(userId, "New Account", "New Type", "000111222", "000111222", "DKK", 0.50, 0).getId();
		System.out.println();
		testCreateTransaction(accountId, "New Transaction", 100);
		System.out.println();
		testUpdateUser(userId, "New Consultant", USER.CONSULTANT);
		printUser(DB.getUserByCpr("123456-7890"));
		System.out.println();
		testUpdateAccount(accountId, "New-New Type", ACCOUNT.TYPE);
		testUpdateAccount(accountId, "0.2", ACCOUNT.INTEREST);
		printAccount(DB.getAccountByNumber("000111222"));
		System.out.println();
		testDeleteAccount(accountId);
		System.out.println();
		testDeleteAccountByNumber("000111222");
		System.out.println();
		testDeleteUser("NewUser");
		System.out.println();
		testDeleteUserByCpr("123456-7890");
		
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

	private static void testUpdateAccount(int accountId, String value, ACCOUNT attribute) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.updateAccount(accountId, value, attribute);
		System.out.println("Updated New Account: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testUpdateUser(int userId, String value, USER attribute) throws SQLException {
		long start = System.currentTimeMillis();
		boolean success = DB.updateUser(userId, value, attribute);
		System.out.println("Updated New User: " + success);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static Transaction testCreateTransaction(int accountId, String description, double amount) throws SQLException {
		long start = System.currentTimeMillis();
		Transaction transaction = DB.createTransaction(accountId, description, amount);
		System.out.println("Created Transaction, Query Time: " + (System.currentTimeMillis()-start));
		printTransaction(transaction);
		return transaction;
	}

	private static Account testCreateAccount(int userId, String name, String type, String number, String iban, String currency, double interest, double balance) throws SQLException {
		long start = System.currentTimeMillis();
		Account account = DB.createAccount(userId, name, type, number, iban, currency, interest, balance);
		System.out.println("Created Account, Query Time: " + (System.currentTimeMillis()-start));
		printAccount(account);
		return account;
	}

	private static User testCreateUser(String username, String password, String cpr, String name, String institute, String consultant) throws SQLException {
		long start = System.currentTimeMillis();
		User user = DB.createUser(username, password, cpr, name, institute, consultant);
		System.out.println("Create User, Query Time: " + (System.currentTimeMillis()-start));
		printUser(user);
		return user;
	}

	public static ArrayList<Transaction> testGetTransactions(int id) throws SQLException{
		long start = System.currentTimeMillis();
		ArrayList<Transaction> transactions = DB.getTransactions(id);
		System.out.println("Got Transactions, Query Time: " + (System.currentTimeMillis()-start));
		for (Transaction transaction : transactions)
			printTransaction(transaction);
		return transactions;
	}

	public static ArrayList<Account> testGetAccounts(int id) throws SQLException{
		long start = System.currentTimeMillis();
		ArrayList<Account> accounts = DB.getAccounts(id);
		System.out.println("Got Accounts, Query Time: " + (System.currentTimeMillis()-start));
		for (Account account : accounts)
			printAccount(account);
		return accounts;
	}
	
	public static User testGetUser(int userId) throws SQLException{
		long start = System.currentTimeMillis();
		User user = DB.getUser(userId);
		System.out.println("Got Users, Query Time: " + (System.currentTimeMillis()-start));
		printUser(user);
		return user;
	}
	
	public static ArrayList<User> testGetUsers() throws SQLException{
		long start = System.currentTimeMillis();
		ArrayList<User> users = DB.getUsers();
		System.out.println("Got Users, Query Time: " + (System.currentTimeMillis()-start));
		for (User user : users)
			System.out.println(user.getId()+" : "+user.getUsername());
		return users;
	}
	
	public static int testCheckLogin(String username, String password) throws SQLException, InputException {
		long start = System.currentTimeMillis();
		int id = DB.checkLogin(username, password);
		System.out.println("Checklogin as Thomas: " + id);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
		return id;
	}
	
	public static void printUser(User user){
		System.out.println(user.getId() +" : "+ user.getUsername() +" : "+ user.getCpr() +" : "+ user.getName() +" : "+ user.getInstitute() +" : "+ user.getConsultant());
	}
	
	private static void printAccount(Account account) {
		System.out.println(account.getUserId() +" : "+ account.getId() +" : "+ account.getName() +" : "+ account.getType() +" : "+ account.getNumber() +" : "+ account.getIban() +" : "+ account.getCurrency() +" : "+ account.getInterest());
	}
	
	private static void printTransaction(Transaction transaction) {
		System.out.println(transaction.getAccountId() +" : "+ transaction.getId() +" : "+ transaction.getDateAsString() +" : "+ transaction.getDescription() +" : "+ transaction.getAmount());
	}

	//Fields
	private Connection connection;
	private String url = "jdbc:db2://192.86.32.54:5040/DALLASB:retrieveMessagesFromServerOnGetMessage=true;emulateParameterMetaDataForZCalls=1;";
	private String user = "DTU18";
	private String pass = "FAGP2016";
	
	//Constructor
	public DB2Test() throws SQLException, ClassNotFoundException {
		try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("DB2 Driver not found.");
            throw e;
        }
		this.connection = DriverManager.getConnection(url,user,pass);
	}
	
	//Methods
	@SuppressWarnings("unused")
	private PreparedStatement prepareStatement(String string) throws SQLException {
		return connection.prepareStatement(string);
	}
	
	@SuppressWarnings("unused")
	private ResultSet executeQuery(PreparedStatement statement) throws SQLException {
		return statement.executeQuery();
	}
	
	@SuppressWarnings("unused")
	private ResultSet executeQuery(String statement) throws SQLException {
		return connection.createStatement().executeQuery(statement);
	}
	
}
