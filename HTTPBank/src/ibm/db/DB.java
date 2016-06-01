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
import java.util.Comparator;
import java.util.Properties;

/**
 * A class for handling all Database queries required by the HTTPBank.
 * Class uses PreparedStatements for easy implementation and efficiency.
 */
public class DB {
	//Static Constructor
	static {
		try {
			getConnection();
		} catch (SQLException e) {
			// A Database access error occurred. Can't really do anything about it at this point.
		}
	}
	
	//Fields
	private static Connection connection;
	private static final String url = "jdbc:db2://192.86.32.54:5040/DALLASB";
	
	/*
	 * DONE: Transaction for account number and iban (senderAmount, receiverAmount),
	 * DONE: update accounts, no currency conversion.
	 * Procedures, trigger and events.
	 * TODO: SQL-exception handling.
	 * DONE: Sort transactions by date.
	 */
	
	//Methods
	// GET Methods
	/**
	 * Queries the database and returns a list of transactions related to a specific account-id.
	 * @return ArrayList containing all current database transactions related to the given account. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<Transaction> getTransactions(int accountid) throws SQLException {
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
		
		//Sorts all Transactions by Date.
		resultList.sort(new Comparator<Transaction>(){
			@Override
			public int compare(Transaction o1, Transaction o2) {
				return Long.compare(o1.getDateRaw(), o2.getDateRaw());
			}
		});
		
		return resultList;
	}
	
	/**
	 * Queries the database and returns a list of accounts related to a specific user-id.
	 * @return ArrayList containing all current database accounts related to the given user. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<Account> getAccounts(int userid) throws SQLException {
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
	 * Queries the database and returns the account with the given account id and data associated with it.
	 * @throws SQLException 
	 */
	public static Account getAccount(int accountId) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
				+ "FROM DTUGRP07.ACCOUNTS "
				+ "WHERE ACCOUNT_ID = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, accountId);
		
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
	
	/**
	 * Queries the database and returns a list of users.
	 * Note: For now the user objects returned by this method only return the 'userId' and 'username'. No other information is received.
	 * @return ArrayList containing all current database users. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<User> getUsers() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, USERNAME, CPR, NAME "
				+ "FROM DTUGRP07.USERS;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		
		ArrayList<User> resultList = null;
		if (statement.execute()){ //If query is successful, create list of users.
			resultList = new ArrayList<User>();
			ResultSet results = statement.getResultSet();
			while (results.next()){ //Fetch usernames from results and add to resultlist.
				resultList.add(new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4)));
			}
		}
		statement.close();
		return resultList;
	}
	
	/**
	 * Returns the user with the given userId and all information related to them.
	 * @return User object containing all fields except for 'accounts'. Returns null if database query fails or returns no rows.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static User getUser(int userId) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
				+ "FROM DTUGRP07.USERS "
				+ "WHERE USER_ID = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, userId);
		
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
		PreparedStatement statement = connection.prepareStatement("SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
				+ "FROM DTUGRP07.ACCOUNTS "
				+ "WHERE NUMBER = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
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
	 * Enums to specifying which method to use to create the transaction.
	 */
	public static enum TransBy {
		ID("ACCOUNT_ID"),
		NUMBER("NUMBER"),
		IBAN("IBAN");
		
		private String string;
		
		TransBy(String string){
			this.string = string;
		}
		
		public String toString(){
			return string;
		}
	}
	
	/**
	 * Queries the database to insert a new transaction into the TRANSACTIONS table.
	 * No manipulation of the 'amount' parameters is done, meaning the 'amount' should precisely reflect how the account's balance should be changed, negative if subtracting.
	 * @param transBy TransBy, defines the method the create the transaction, defines what 'receiverId' contains.
	 * @param senderId The accountId for the account sending this transfer.
	 * @param receiverId The accountId, account-number or IBAN for the account receiving this transfer. Should match the 'transBy' parameter.
	 * @param senderDescription The string with the description of the transfer for the sender.
	 * @param receiverDescription The string with the description of the transfer for the receiver.
	 * @param senderAmount The amount to enter into the transaction for the sender.
	 * @param receiverAmount The amount to enter into the transaction for the receiver.
	 * @return The new transaction for the sender as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws SQLException 
	 */
	public static Transaction createTransaction(TransBy transBy, int senderId, String receiverId, String senderDescription, String receiverDescription, double senderAmount, double receiverAmount) throws SQLException{
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		
		Transaction senderTransaction = null;
		@SuppressWarnings("unused")
		Transaction receiverTransaction = null;
		try {
			connection.setAutoCommit(false);
			
			senderTransaction = createTransaction(transBy, date, ""+senderId, senderDescription, senderAmount);
			receiverTransaction = createTransaction(transBy, date, receiverId, receiverDescription, receiverAmount);
			
			//if (receiverTransaction == null) ; //The receiver was not an account we know about.
			
			connection.commit();
		} catch (Exception e){
			//Error, rollback all changes.
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		
		return senderTransaction;
	}
	
	/**
	 * Queries the database to insert a new transaction into the TRANSACTIONS table.
	 * Note that no change or checks are made to any accounts with this method.
	 * Uses the 'ID' method to create the transaction as the 'ACCOUNTNUMBER' and 'IBAN' method allow the account to not exist and this method does not allow this.
	 * @param accountId The accountId for the account to have the transaction.
	 * @param description The string with the description of the transaction.
	 * @param amount The amount to enter into the transaction, can be negative.
	 * @return The new transaction as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws SQLException 
	 */
	public static Transaction createTransaction(int accountId, String description, double amount) throws SQLException{
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		Transaction transaction = null;
		try {
			connection.setAutoCommit(false);
			transaction = createTransaction(TransBy.ID, date, ""+accountId, description, amount);
			connection.commit();
		} catch (Exception e){
			//Error, rollback all changes.
			connection.rollback();
			throw e;
		} finally {
			connection.setAutoCommit(true);
		}
		return transaction;
	}
	
	/**
	 * Performs multiple queries to create a new transaction. Autocommit should be disabled and commit should be managed by whatever calls this method.
	 */
	private static Transaction createTransaction(TransBy transBy, Date date, String identity, String description, double amount) throws SQLException{
		//Get the balance.
		PreparedStatement getBalanceStatement = connection.prepareStatement("SELECT ACCOUNT_ID, BALANCE "
				+ "FROM DTUGRP07.ACCOUNTS "
				+ "WHERE "+transBy.toString()+" = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		switch(transBy){
		case IBAN:
		case NUMBER:
			getBalanceStatement.setString(1, identity);
			break;
		case ID:
		default:
			getBalanceStatement.setInt(1, Integer.parseInt(identity));
			break;
		}
		getBalanceStatement.execute();
		double balance = 0;
		int id;
		ResultSet results = getBalanceStatement.getResultSet();
		if (results.next()){ //Fetch row if able.
			id = results.getInt(1);
			balance = results.getDouble(2);
		} else {
			//TODO No account found!
			return null;
			//throw new SQLException();
		}
		getBalanceStatement.close();
		
		//Update the balance.
		PreparedStatement updateBalanceStatement = connection.prepareStatement("UPDATE DTUGRP07.ACCOUNTS "
				+ "SET BALANCE = ? "
				+ "WHERE ACCOUNT_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		if ((balance+amount) < 0){
			//TODO Updated balance will be negative.
			//throw new SQLException();
		}
		updateBalanceStatement.setDouble(1, (balance+amount));
		updateBalanceStatement.setInt(2, id);
		updateBalanceStatement.executeUpdate();
		updateBalanceStatement.close();
		
		//Create the transaction.
		PreparedStatement createTransactionStatement = connection.prepareStatement("INSERT INTO DTUGRP07.TRANSACTIONS "
				+ "(ACCOUNT_ID, \"DATE\", DESCRIPTION, AMOUNT) VALUES "
				+ "(?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		createTransactionStatement.setInt(1, id);
		createTransactionStatement.setDate(2, date);
		createTransactionStatement.setString(3, description);
		createTransactionStatement.setDouble(4, amount);
		createTransactionStatement.execute(); //Attempt to insert new row.
		createTransactionStatement.close();
		
		return new Transaction(null, id, date, description, amount);
	}
	
	/**
	 * Queries the database to insert a new transaction into the TRANSACTIONS table.
	 * Note that no change is made to any accounts with this method.
	 * 
	 * @deprecated Use createTransaction(int accountId, String description, double amount).
	 * 
	 * @param accountId The accountId for the account receiving this deposit.
	 * @param description The string with the description of the deposit.
	 * @param amount The amount to enter into this transaction.
	 * 
	 * @return The new transaction as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws SQLException 
	 */
	@Deprecated
	public static Transaction createDeposit(int accountId, String description, double amount) throws SQLException{
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO DTUGRP07.TRANSACTIONS "
				+ "(ACCOUNT_ID, \"DATE\", DESCRIPTION, AMOUNT) VALUES "
				+ "(?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, accountId);
		statement.setDate(2, date);
		statement.setString(3, description);
		statement.setDouble(4, amount);
		statement.execute(); //Attempt to insert new row.
		statement.close();
		
		return new Transaction(null, accountId, date, description, amount);
	}
	
	/**
	 * Queries the database to insert a new transaction into the TRANSACTIONS table.
	 * Note that no change is made to any accounts with this method.
	 * 
	 * @deprecated Use createTransaction(int accountId, String description, double amount).
	 * 
	 * @param accountId The accountId for the account receiving this withdrawal.
	 * @param description The string with the description of the withdrawal.
	 * @param amount The amount to subtract in this transaction.
	 * 
	 * @return The new transaction as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws SQLException 
	 */
	@Deprecated
	public static Transaction createWithdrawal(int accountId, String description, double amount) throws SQLException{
		Date date = new Date(Calendar.getInstance().getTime().getTime());
		
		PreparedStatement statement = connection.prepareStatement("INSERT INTO DTUGRP07.TRANSACTIONS "
				+ "(ACCOUNT_ID, \"DATE\", DESCRIPTION, AMOUNT) VALUES "
				+ "(?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, accountId);
		statement.setDate(2, date);
		statement.setString(3, description);
		statement.setDouble(4, -amount);
		statement.execute(); //Attempt to insert new row.
		statement.close();
		
		return new Transaction(null, accountId, date, description, -amount);
	}
	
	/**
	 * Queries the database to insert a new account into the ACCOUNTS table, then queries the database to return the newly created row.
	 * @return The new account as an Account object with all fields, if successfully created.
	 * @throws SQLException 
	 */
	public static Account createAccount(int userId, String name, String type, String number, String iban, String currency, double interest, double balance) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("INSERT INTO DTUGRP07.ACCOUNTS "
				+ "(USER_ID, \"TYPE\", NAME, NUMBER, IBAN, INTEREST, BALANCE, CURRENCY) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?);"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, userId);
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
	 * Queries the database to update a user's information for the user with the given user id.
	 * @return True if operation was successful.
	 * @throws SQLException 
	 */
	public static boolean updateUser(int userId, String value, USER attribute) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("UPDATE DTUGRP07.USERS "
				+ "SET "+ attribute.toString() +" = ? "
				+ "WHERE USER_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, value);
		statement.setInt(2, userId);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		return true;
	}
	
	/**
	 * Queries the database to update a user's information for the user with the given user id.
	 * @return True if operation was successful.
	 * @throws SQLException 
	 */
	public static boolean updateUser(int userId, String username, String password, String cpr, String name, String institute, String consultant) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("UPDATE DTUGRP07.USERS "
				+ "SET USERNAME = ?, PASSWORD =?, CPR = ?, NAME = ?, INSTITUTE = ?, CONSULTANT = ? "
				+ "WHERE USER_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, username);
		statement.setString(2, password);
		statement.setString(3, cpr);
		statement.setString(4, name);
		statement.setString(5, institute);
		statement.setString(6, consultant);
		statement.setInt(7, userId);
		
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
	 * Queries the database to update account information for the account with the given account id.
	 * @return True if operation was successful.
	 * @throws SQLException 
	 */
	public static boolean updateAccount(int accountId, String value, ACCOUNT attribute) throws SQLException{
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
		
		statement.setInt(2, accountId);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		return true;
	}
	
	/**
	 * Queries the database to update account information for the account with the given account id.
	 * @return True if operation was successful.
	 * @throws SQLException 
	 */
	public static boolean updateAccount(int accountId, String name, String type, String number, String iban, String currency, double interest, double balance) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("UPDATE DTUGRP07.ACCOUNTS "
				+ "SET NAME = ?, TYPE = ?, NUMBER = ?, IBAN = ?, CURRENCY = ?, INTEREST = ?, BALANCE = ? "
				+ "WHERE ACCOUNT_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		
		statement.setString(1, name);
		statement.setString(2, type);
		statement.setString(3, number);
		statement.setString(4, iban);
		statement.setString(5, currency);
		statement.setDouble(6, interest);
		statement.setDouble(7, balance);
		statement.setInt(8, accountId);
		
		statement.execute(); //Attempt to insert new row.
		statement.close();
		return true;
	}
	
	
	// DELETE Methods
	/**
	 * Queries the database to delete the account and any transaction associated with this account with the given account-id.
	 * @return True if operation was successful.
	 */
	public static boolean deleteAccount(int accountId) throws SQLException{
		PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.ACCOUNTS "
				+ "WHERE ACCOUNT_ID = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, accountId);
		
		statement.execute(); //Attempt to delete row.
		statement.close();
		return true;
	}
	
	/**
	 * Queries the database to delete the account and any transaction associated with this account with the given account-number.
	 * @return True if operation was successful.
	 */
	public static boolean deleteAccountByNumber(String number) throws SQLException{
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
	 * Queries a request for user information and checks for correct password. Returns the 'userId' of the user only if user exists and correct password is given as argument, else returns -1.
	 * Uses a PreparedStatement to query.
	 * TODO: This would be better to have as a stored procedure on the database.
	 * @throws SQLException 
	 * @throws InputException If invalid input is given.
	 */
	public static int checkLogin(String username, String password) throws SQLException, InputException {
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
		return connection.createStatement().executeQuery(query);
	}
	
	/**
	 * Checks if there is a current connection to the database and if not attempts to connect to it.
	 * Temporary solution to creating connections and allow troubleshooting till a connection pool is integrated that can be handled reliably.
	 * Note: Should check of the application server has integrated connection pool that can be used, else create our own.
	 * @throws SQLException If connection fails or driver is missing.
	 */
	public static void getConnection() throws SQLException {
		getConnection(false);
	}
	
	/**
	 * Checks if there is a current connection to the database and if not attempts to connect to it.
	 * Temporary solution to creating connections and allow troubleshooting till a connection pool is integrated that can be handled reliably.
	 * Note: Should check of the application server has integrated connection pool that can be used, else create our own.
	 * @throws SQLException If connection fails or driver is missing.
	 */
	public static void getConnection(boolean forceNew) throws SQLException {
		if (forceNew || connection == null){
			try {
	            Class.forName("com.ibm.db2.jcc.DB2Driver");
	        } catch (ClassNotFoundException e) {
	            throw new SQLException(e);
	        }
			if (connection == null){
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
			connection = DriverManager.getConnection(url, getProperties());
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
	
	/**
	 * Handle SQLException, possibly try to fix the problem or if not possible, throw exception.
	 * @return Returns true if a fix was deployed and a retry could possibly be successful (Not necessarily, beware infinite loop!), false if no fix was deployed.
	 */
	private static boolean handleSQLException(SQLException e){
		//TODO Handle Exception.
		
		return true;
	}
}
