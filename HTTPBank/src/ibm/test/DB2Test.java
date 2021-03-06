package ibm.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ibm.db.DB;
import ibm.db.DB.ACCOUNT;
import ibm.db.DB.USER;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.InputException;
import ibm.resource.Message;
import ibm.resource.Transaction;
import ibm.resource.User;

public class DB2Test {
	//Main
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InputException {
		
		String username = "TestGetUser";
		String cpr = "TestGS1234";
		String name = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		DB.deleteUserByCpr(cpr);
		//Create a user to get
		User user = DB.createUser(username, cpr, name, institute, consultant);
		assertNotNull(user);
		//Get user
		User sameUser = DB.getUser(user.getId());
		assertNotNull(sameUser);
		//Assert
		System.out.println(user.getId() +","+ sameUser.getId());
		System.out.println(user.getConsultant() +","+ sameUser.getConsultant());
		System.out.println(user.getCpr() +","+ sameUser.getCpr());
		System.out.println(user.getInstitute() +","+ sameUser.getInstitute());
		System.out.println(user.getName() +","+ sameUser.getName());
		System.out.println(user.getUsername() +","+ sameUser.getUsername());
		
		//testCheckLogin("Helgo","moo");
		//testSearchUsers("Thomas", "01");
		//System.out.println();
		//testCreateTransaction(1, "New Transaction1", -100);
		//testSearchArchive(1,"2015-04-04 00:00:00","2016-06-14 14:00:01");
		testCreateTransaction(2, "3", "Should be goooood","Should be great", 30, "DKK");
		//testBatchTimer();
		//System.out.println();
		//testCreateMessage("Hej Helge", 1, "1", DB.TransBy.ID);
		//System.out.println();
		//testGetArchive(1);
		//testGetReceiverCurrency("0002", DB.TransBy.NUMBER);
		//changes
//		testCheckLogin("Thomas", "1234");
//		System.out.println();
//		
//		
//		
//		testGetUsers();
//		
//		System.out.println();
//		int userId = testCreateUser("NewUser", "user", "123456-7890", "User", "NewInstitute", "").getId();
//		System.out.println();
//		testGetUser(userId);
//		System.out.println();
//		int senderId = testCreateAccount(userId, "New Account", "New Type", "000111222", "000111222", "DKK", 0.50, 100).getId();
//		int receiverId = testCreateAccount(userId, "Moo Account", "New Type", "000111333", "000111333", "DKK", 0.55, 0).getId();
//		System.out.println();
//		testGetAccounts(userId);
//		System.out.println();
//		testCreateTransaction(senderId, receiverId, "New Transaction", 100);
//		System.out.println();
//		testGetTransactions(senderId);
//		testGetTransactions(receiverId);
//		System.out.println();
//		testGetAccounts(userId);
//		System.out.println();
//		testUpdateUser(userId, "New Consultant", USER.CONSULTANT);
//		printUser(DB.getUserByCpr("123456-7890"));
//		System.out.println();
//		testUpdateAccount(receiverId, "New-New Type", ACCOUNT.TYPE);
//		testUpdateAccount(receiverId, "0.2", ACCOUNT.INTEREST);
//		printAccount(DB.getAccountByNumber("000111222"));
//		System.out.println();
//		testDeleteAccount(receiverId);
//		System.out.println();
//		testDeleteAccountByNumber("000111222");
//		System.out.println();
//		testDeleteUser("NewUser");
//		System.out.println();
//		testDeleteUserByCpr("123456-7890");
		//System.out.println();
		
		//testArchive();
		

	}
	
	private static void testGetReceiverCurrency(String receiverID, DB.TransBy transby) throws DatabaseException {
		String result = DB.getReceiverCurrency(receiverID, transby);
		System.out.println(result);
	}
	
	private static void testSearchUsers(String name) throws DatabaseException {
		ArrayList<User> array = DB.searchUsers(name);
		for(User u : array) {
			printUser(u);
		}
		if(array.isEmpty()) {
			System.out.println("empty");
		}
	}
	
	private static void testSearchArchive(int userID, long dateFrom, long dateTo) throws DatabaseException {		
		ArrayList<Transaction> array = DB.searchArchive(userID, dateFrom, dateTo);
		for(Transaction t : array) {
			printTransaction(t);
		}
		if(array.isEmpty()) {
			System.out.println("empty");
		}
	}
	
	private static void testGetArchive(int account_id) throws DatabaseException {
		ArrayList<Transaction> array = DB.getArchive(account_id);
		for(Transaction t : array) {
			System.out.println(t.getAccountId() +  " " + t.getAmount() + " " + t.getDescription());
		}
	}

	private static void testArchive() throws SQLException {
		DB.archiveTransactions();
	}
	
	private static void testCreateMessage(String message, int userID, String receiver, DB.TransBy transBy) throws SQLException {
		DB.createMessage(message, userID, receiver, transBy);
	}
	
	private static void testGetCurrency() throws DatabaseException{
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

	private static void testDeleteUser(int userId) throws SQLException {
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
		Account account = DB.updateAccount(accountId, value, attribute);
		System.out.println("Updated New Account: " + account.getNumber());
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testUpdateUser(int userId, String value, USER attribute) throws SQLException {
		long start = System.currentTimeMillis();
		User user = DB.updateUser(userId, value, attribute);
		System.out.println("Updated New User: " + user.getName());
		System.out.println("Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testCreateTransaction(int senderId, String description, double amount, String currency) throws SQLException {
		long start = System.currentTimeMillis();
		boolean account = DB.createTransaction(senderId, description, amount, currency);
		System.out.println("Created Transaction "+ account +", Query Time: " + (System.currentTimeMillis()-start));
	}
	
	private static void testCreateTransaction(int senderId, String receiverId, String senderDescription, String receiverDescription, double senderAmount, String currency) throws SQLException {
		long start = System.currentTimeMillis();

		boolean transaction = DB.createTransaction(DB.TransBy.ID, senderId, ""+receiverId, senderDescription, receiverDescription, -senderAmount, currency);
		System.out.println("Created Transaction "+transaction+", Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testCreateAccount(int userId, String name, String type, String number, String iban, String currency, double interest, double balance) throws SQLException {
		long start = System.currentTimeMillis();
		Account account = DB.createAccount(userId, name, type, number, iban, currency, interest);
		System.out.println("Created Account "+account.getNumber()+", Query Time: " + (System.currentTimeMillis()-start));
	}

	private static void testCreateUser(String username, String cpr, String name, String institute, String consultant) throws SQLException {
		long start = System.currentTimeMillis();
		User user = DB.createUser(username, cpr, name, institute, consultant);

		System.out.println("Create User "+user+", Query Time: " + (System.currentTimeMillis()-start));
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
		ArrayList<User> users = DB.getUsers(0);
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
		System.out.println(account.getUserId() +" : "+ account.getId() +" : "+ account.getName() +" : "+ account.getType() +" : "+ account.getNumber() +" : "+ account.getIban() +" : "+ account.getCurrency() +" : "+ account.getInterest() +" : "+account.getBalance());
	}
	
	private static void printTransaction(Transaction transaction) {
		System.out.println(transaction.getAccountId() +" : "+ transaction.getDescription() +" : "+ transaction.getAmount());
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
