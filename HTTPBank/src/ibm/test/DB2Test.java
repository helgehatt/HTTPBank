package ibm.test;

import ibm.db.DB;
import ibm.db.DB.ACCOUNT;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.InputException;
import ibm.resource.Transaction;
import ibm.resource.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class DB2Test {
	//Main
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InputException {
		//testCheckLogin2("Helgo","moo");
		//System.out.println();
		testCreateTransaction2(27, 28, "Should be goooood","Should be great", 300, 3);
		//testBatchTimer();
		System.out.println();
		//testCreateMessage("Tak for igår <3\nDet var sgu billigt", "Xerxes", 3);
		//System.out.println();
		//testGetArchive(1);
		/*testCheckLogin("Thomas", "1234");
		System.out.println();
		
		testGetUsers();
		
		System.out.println();
		int userId = testCreateUser("NewUser", "user", "123456-7890", "User", "NewInstitute", null).getId();
		System.out.println();
		testGetUser(userId);
		System.out.println();
		int senderId = testCreateAccount(userId, "New Account", "New Type", "000111222", "000111222", "DKK", 0.50, 100).getId();
		int receiverId = testCreateAccount(userId, "Moo Account", "New Type", "000111333", "000111333", "DKK", 0.55, 0).getId();
		System.out.println();
		testGetAccounts(userId);
		System.out.println();
		testCreateTransaction(senderId, receiverId, "New Transaction", 100);
		System.out.println();
		testGetTransactions(senderId);
		testGetTransactions(receiverId);
		System.out.println();
		testGetAccounts(userId);
		System.out.println();
		testUpdateUser(userId, "New Consultant", USER.CONSULTANT);
		printUser(DB.getUserByCpr("123456-7890"));
		System.out.println();
		testUpdateAccount(receiverId, "New-New Type", ACCOUNT.TYPE);
		testUpdateAccount(receiverId, "0.2", ACCOUNT.INTEREST);
		printAccount(DB.getAccountByNumber("000111222"));
		System.out.println();
		testDeleteAccount(receiverId);
		System.out.println();
		testDeleteAccountByNumber("000111222");
		System.out.println();
		testDeleteUser("NewUser");
		System.out.println();
		testDeleteUserByCpr("123456-7890"); */
		//System.out.println();
		
		//testArchive();
		
	}
	
	private static void testGetArchive(int account_id) {
		ArrayList<Transaction> array = DB.getArchive(account_id);
		for(Transaction t : array) {
			System.out.println(t.getId() + " " + t.getAccountId() + " " + t.getDateAsString() + " " + t.getAmount() + " " + t.getDescription());
		}
	}

	private static void testArchive() throws SQLException {
		DB.archiveTransactions();
	}
	
	private static void testBatchTimer() throws SQLException {
		DB.startBatchTimer();
		
	}
	
	private static void testCreateMessage(String message, String senderName, int userID) throws SQLException {
		DB.createMessage(message, senderName, userID);
	}
	
	private static void testGetCurrency(){
		long start = System.currentTimeMillis();
		ArrayList<String> list = DB.getCurrencies();
		for (String string : list) System.out.println(string);
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

	private static Transaction testCreateTransaction(int senderId, int receiverId, String description, double amount) throws SQLException {
		long start = System.currentTimeMillis();
		Transaction transaction = DB.createTransaction(DB.TransBy.ID, senderId, ""+receiverId, description, description, -amount, amount);
		System.out.println("Created Transaction, Query Time: " + (System.currentTimeMillis()-start));
		printTransaction(transaction);
		return transaction;
	}
	
	private static Transaction testCreateTransaction2(int senderId, int receiverId, String senderDescription, String receiverDescription, double senderAmount, double receiverAmount) throws SQLException {
		long start = System.currentTimeMillis();
		Transaction transaction = DB.createTransaction2(DB.TransBy.ID, senderId, ""+receiverId, senderDescription, receiverDescription, -senderAmount, receiverAmount);
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
	
	public static int testCheckLogin2(String username, String password) throws SQLException, InputException {
		long start = System.currentTimeMillis();
		int id = DB.checkLogin2(username, password);
		System.out.println("Checklogin as Thomas: " + id);
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
		return id;
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
