package ibm.db;

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
import java.util.Properties;

/**
 * A class for handling all Database queires required by the HTTPBank.
 * Class uses PreparedStatements for easy implementation and efficiency.
 * 
 * Method TODO list:
 * What methods do we need in here? Inserts, Updates, Selects, Procedures, stuff?
 */
public class DB {
	//Fields
	private static Connection connection;
	private static final String url = "jdbc:db2://192.86.32.54:5040/DALLASB";
	
	//Methods
	// GET Methods
	/**
	 * Queries the database and returns a list of transactions related to a specific account-id.
	 * @return ArrayList containing all current database transactions related to the given account. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<Transaction> getTransactions(int accountid) throws SQLException {
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT TRANSACTION_ID, ACCOUNT_ID, DATE, DESCRIPTION, AMOUNT "
				+ "FROM DTUGRP07.TRANSACTIONS "
				+ "WHERE ACCOUNT_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setLong(1, accountid); //Sets the first '?' parameter to the integer 'accountid'.
		
		ArrayList<Transaction> resultList = null;
		if (statement.execute()){ //If query is successful, create list of transactions.
			resultList = new ArrayList<Transaction>();
			ResultSet results = statement.getResultSet();
			while (results.next()){ //Fetch transaction-id from results and add to resultlist.
				resultList.add(new Transaction(results.getLong(1), results.getInt(2), results.getDate(3), results.getString(4), results.getDouble(5)));
			}
		}
		statement.close();
		return resultList;
	}
	
	/**
	 * Queries the database and returns a list of accounts related to a specific user-id.
	 * @return ArrayList containing all current database accounts related to the given user. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<Account> getAccounts(int userid) throws SQLException {
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
				+ "FROM DTUGRP07.ACCOUNTS "
				+ "WHERE USER_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setLong(1, userid); //Sets the '?' parameter to the integer 'userid'.
		
		ArrayList<Account> resultList = null;
		if (statement.execute()){ //If query is successful, create list of accounts.
			resultList = new ArrayList<Account>();
			ResultSet results = statement.getResultSet();
			while (results.next()){ //Fetch accountnames from results and add to resultlist.
				resultList.add(new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9)));
			}
		}
		statement.close();
		return resultList;
	}
	
	/**
	 * Queries the database and returns a list of users.
	 * Note: For now the user objects returned by this method only return the 'user_id' and 'username'. No other information is received.
	 * @return ArrayList containing all current database users. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<User> getUsers() throws SQLException {
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, USERNAME "
				+ "FROM DTUGRP07.USERS;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		
		ArrayList<User> resultList = null;
		if (statement.execute()){ //If query is successful, create list of users.
			resultList = new ArrayList<User>();
			ResultSet results = statement.getResultSet();
			while (results.next()){ //Fetch usernames from results and add to resultlist.
				resultList.add(new User(results.getInt(1), results.getString(2)));
			}
		}
		statement.close();
		return resultList;
	}
	
	/**
	 * Returns the user with the given user_id and all information related to them.
	 * @return User object containing all fields except for 'accounts'. Returns null if database query fails or returns no rows.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static User getUser(int user_id) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
				+ "FROM DTUGRP07.USERS "
				+ "WHERE USER_ID = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, user_id);
		
		User user = null;
		if (statement.execute()){ //If query is successful, attempt to create user.
			ResultSet results = statement.getResultSet();
			if (results.next()){ //Fetch row if able.
				user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
			}
		}
		statement.close();
		
		return user;
	}
	
	/**
	 * Returns the user with the given cpr-number and all information related to them.
	 * @return User object containing all fields except for 'accounts'. Returns null if database query fails or returns no rows.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static User getUserByCpr(String cpr) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
				+ "FROM DTUGRP07.USERS "
				+ "WHERE CPR = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, cpr);
		
		User user = null;
		if (statement.execute()){ //If query is successful, attempt to create user.
			ResultSet results = statement.getResultSet();
			if (results.next()){ //Fetch row if able.
				user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
			}
		}
		statement.close();
		
		return user;
	}
	
	/**
	 * Queries the database for a specific account with the given account number. 
	 * @throws SQLException 
	 */
	public static Account getAccountByNumber(String number) throws SQLException {
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
				+ "FROM DTUGRP07.ACCOUNTS "
				+ "WHERE NUMBER = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, number);
		
		Account account = null;
		if (statement.execute()){ //If query is successful, attempt to create account object.
			ResultSet results = statement.getResultSet();
			if (results.next()){ //Fetch row if able.
				account = new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9));
			}
		}
		statement.close();
		
		return account;
	}
	
	
	// CREATE Methods
	/**
	 * Queries the database to insert a new transaction into the TRANSACTIONS table.
	 * @return The new transaction as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws SQLException 
	 */
	public static Transaction createTransaction(int account_id, String description, double amount) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO DTUGRP07.TRANSACTIONS "
				+ "(ACCOUNT_ID, \"DATE\", DESCRIPTION, AMOUNT) VALUES "
				+ "(?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, account_id);
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		statement.setDate(2, date);
		statement.setString(3, description);
		statement.setDouble(4, amount);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		
		return new Transaction(null, account_id, date, description, amount);
	}
	
	/**
	 * Queries the database to insert a new account into the ACCOUNTS table, then queries the database to return the newly created row.
	 * @return The new account as an Account object with all fields, if successfully created.
	 * @throws SQLException 
	 */
	public static Account createAccount(int user_id, String name, String type, String number, String iban, String currency, double interest, double balance) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO DTUGRP07.ACCOUNTS "
				+ "(USER_ID, \"TYPE\", NAME, NUMBER, IBAN, INTEREST, BALANCE, CURRENCY) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, user_id);
		statement.setString(2, type);
		statement.setString(3, name);
		statement.setString(4, number);
		statement.setString(5, iban);
		statement.setDouble(6, interest);
		statement.setDouble(7, balance);
		statement.setString(8, currency);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		
		Account account = getAccountByNumber(number);
		account.setTransactions(new ArrayList<Transaction>());
		return account;
	}
	
	/**
	 * Queries the database to insert a new user into the USERS table, then queries the database to return the newly created row.
	 * @return The new user as a User object with all fields, if successfully created.
	 * @throws SQLException 
	 */
	public static User createUser(String username, String password, String cpr, String name, String institute, String consultant) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO DTUGRP07.USERS "
				+ "(USERNAME, PASSWORD, CPR, NAME, INSTITUTE, CONSULTANT) VALUES "
				+ "(?, ?, ?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, username);
		statement.setString(2, password);
		statement.setString(3, cpr);
		statement.setString(4, name);
		statement.setString(5, institute);
		statement.setString(6, consultant);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		
		User user = getUserByCpr(cpr);
		user.setAccounts(new ArrayList<Account>());
		return user;
	}
	
	
	// UPDATE Methods
	/**
	 * Enums to specifying which user attribute to update.
	 */
	public static enum USER {
		USERNAME("USERNAME"),
		PASSWORD("PASSWORD"),
		CPR("CPR"),
		NAME("NAME"),
		INSTITUTE("INSTITUTE"),
		CONSULTANT("CONSULTANT");
		
		private String string;
		
		USER(String string){
			this.string = string;
		}
		
		public String toString(){
			return string;
		}
	}
	
	/**
	 * Queries the database to update a user's information.
	 * @return True if operation was successful.
	 * @throws SQLException 
	 */
	public static boolean updateUser(int user_id, String value, USER attribute) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("UPDATE DTUGRP07.USERS "
				+ "SET "+ attribute.toString() +" = ? "
				+ "WHERE USER_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, value);
		statement.setInt(2, user_id);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		return true;
	}
	
	/**
	 * Enums to specifying which account attribute to update.
	 */
	public static enum ACCOUNT {
		NAME("NAME"),
		TYPE("TYPE"),
		NUMBER("NUMBER"),
		IBAN("IBAN"),
		INTEREST("INTEREST"),
		BALANCE("BALANCE"),
		CURRENCY("CURRENCY");
		
		private String string;
		
		ACCOUNT(String string){
			this.string = string;
		}
		
		public String toString(){
			return string;
		}
	}
	
	/**
	 * Queries the database to update account information.
	 * @return True if operation was successful.
	 * @throws SQLException 
	 */
	public static boolean updateAccount(int account_id, String value, ACCOUNT attribute) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("UPDATE DTUGRP07.ACCOUNTS "
				+ "SET "+ attribute.toString() +" = ? "
				+ "WHERE ACCOUNT_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		
		switch (attribute){
		case NAME:
		case TYPE:
		case NUMBER:
		case IBAN:
		case CURRENCY:
			statement.setString(1, value);
			break;
		case INTEREST:
		case BALANCE:
			statement.setDouble(1, Double.parseDouble(value));
			break;
		}
		
		statement.setInt(2, account_id);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		return true;
	}
	
	
	// DELETE Methods
	/**
	 * Queries the database to delete the account and any transaction associated with this account with the given account-id.
	 * @return True if operation was successful.
	 */
	public static boolean deleteAccount(int account_id) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.ACCOUNTS "
				+ "WHERE ACCOUNT_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, account_id);
		
		statement.execute(); //Attempt to delete row.
		statement.close();
		return true;
	}
	
	/**
	 * Queries the database to delete the account and any transaction associated with this account with the given account-number.
	 * @return True if operation was successful.
	 */
	public static boolean deleteAccountByNumber(String number) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.ACCOUNTS "
				+ "WHERE NUMBER = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, number);
		
		statement.execute(); //Attempt to delete row.
		statement.close();
		return true;
	}
	
	/**
	 * Queries the database to delete the user with the given username.
	 * @return True if operation was successful.
	 */
	public static boolean deleteUser(String username) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.USERS "
				+ "WHERE USERNAME = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, username);
		
		statement.execute(); //Attempt to delete row.
		statement.close();
		return true;
	}
	
	/**
	 * Queries the database to delete the user with the given cpr-number.
	 * @return True if operation was successful.
	 */
	public static boolean deleteUserByCpr(String cpr) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.USERS "
				+ "WHERE CPR = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, cpr);
		
		statement.execute(); //Attempt to delete row.
		statement.close();
		return true;
	}
	
	
	// GENERAL Methods
	/**
	 * Queries a request for user information and checks for correct password. Returns the 'user_id' of the user only if user exists and correct password is given as argument, else returns -1.
	 * Uses a PreparedStatement to query.
	 * TODO: This would be better to have as a stored procedure on the database.
	 * @throws SQLException 
	 * @throws InputException If invalid input is given.
	 */
	public static int checkLogin(String username, String password) throws SQLException, InputException {
		checkConnection();
		
		if (username.matches("\\s")) throw new InputException("Invalid username."); //Checks for white space.
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, USERNAME, PASSWORD "
				+ "FROM DTUGRP07.USERS "
				+ "WHERE USERNAME = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, username); //Sets the '?' parameter in the SQL statement to be the string 'username'.
		
		int passwordIsCorrect = -1;
		if (statement.execute()){ //Executes the SQL statement, returns false if failed for any reason.
			ResultSet result = statement.getResultSet();
			if (result.next()){ //Moves the cursor to first row, returns false if cursor is moved past the last row. Note: There should be 0 or 1 row.
				String correctPassword = result.getString("PASSWORD");
				
				if (password.equals(correctPassword)) //Checks if password is correct.
					passwordIsCorrect = result.getInt(1);
			}
		}
		statement.close();
		return passwordIsCorrect; //Returns -1 if any of the above if statements returns false.
	}
	
	/**
	 * For executing your own queries using this connection.
	 * Mostly for testing/experimentation as you need to handle the ResultSet yourself if you want the result.
	 * @throws SQLException From {@link #checkConnection() checkConnection()}
	 */
	public static ResultSet getQuery(String query) throws SQLException {
		checkConnection();
		return connection.createStatement().executeQuery(query);
	}
	
	/**
	 * Checks if there is a current connection to the database and if not attempts to connect to it.
	 * Temporary solution to creating connections and allow troubleshooting till a connection pool is integrated that can be handled reliably.
	 * Note: Should check of the application server has integrated connection pool that can be used, else create our own.
	 * @throws SQLException If connection fails or driver is missing.
	 */
	public static void checkConnection() throws SQLException {
		if (connection == null){
			try {
	            Class.forName("com.ibm.db2.jcc.DB2Driver");
	        } catch (ClassNotFoundException e) {
	            throw new SQLException(e);
	        }
			connection = DriverManager.getConnection(url, getProperties());
			Runtime.getRuntime().addShutdownHook(new Thread(){ //ShutdownHook for closing resources used by the connection.
				@Override
				public void run(){
					if (connection != null){
						try {
							connection.close();
						} catch (SQLException e) {}
					}
				}
			});
		}
	}
	
	/**
	 * Puts essential connection information into a properties object, can be used to connect to the DB2 Database.
	 * Can be coded to be encrypted, read from file and/or otherwise protected later, if believed necessary.
	 */
	private static Properties getProperties(){
		Properties properties = new Properties();
		properties.put("user", "DTU18");
		properties.put("password", "FAGP2016");
		properties.put("retreiveMessagesFromServerOnGetMessage", "true");
		properties.put("emulateParameterMetaDataForZCalls", "1");
		return properties;
	}
}
