package ibm.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.Message;
import ibm.resource.Transaction;
import ibm.resource.User;

/**
 * A class for handling all Database queries required by the HTTPBank.
 * Class uses PreparedStatements for easy implementation, greater efficiency and security.
 */
public class DB {
	@Resource(name = "jdbc/exampleDS")
	static DataSource ds1;
	
	//Static Constructor
	static {
		for (int tries = 2; 0 < tries; tries--){
			try {
				getConnection();
			} catch (SQLException e) {
				// A Database access error occurred. Can't really do anything about it at this point.
				e.printStackTrace();
			}
		}
	}
	
	//Fields
	private static Connection connection;
	private static final String url = "jdbc:db2://192.86.32.54:5040/DALLASB";
	
	//Methods
	// GET Methods
	/**
	 * Queries the database and returns a list of transactions related to a specific account-id.
	 * @return ArrayList containing all current database transactions related to the given account. Returns null if database query fails.
	 * @throws DatabaseException If a database error occurs.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<Transaction> getTransactions(int accountid) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT TRANSACTION_ID, ACCOUNT_ID, DATE, DESCRIPTION, AMOUNT "
						+ "FROM DTUGRP07.TRANSACTIONS "
						+ "WHERE ACCOUNT_ID = ? "
						+ "ORDER BY DATE DESC;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setLong(1, accountid); //Sets the first '?' parameter to the integer 'accountid'.
				
				ArrayList<Transaction> resultList = null;
				statement.execute(); //Perform query and create list of transactions.
				resultList = new ArrayList<Transaction>();
				ResultSet results = statement.getResultSet();
				while (results.next()){ //Fetch transaction-id from results and add to resultlist.
					resultList.add(new Transaction(results.getLong(1), results.getInt(2), results.getTimestamp(3), results.getString(4), results.getDouble(5)));
				}
				
				//Sorts all Transactions by Date.
				//Collections.sort(resultList, transactionComparator);
				
				return resultList;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch (SQLException e) {
					//Should never be thrown.
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database and returns a list of accounts related to a specific user-id.
	 * @return ArrayList containing all current database accounts related to the given user. Returns null if database query fails.
	 * @throws DatabaseException If a database error occurs.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<Account> getAccounts(int userid) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
						+ "FROM DTUGRP07.ACCOUNTS "
						+ "WHERE USER_ID = ? "
						+ "ORDER BY ACCOUNT_ID;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setLong(1, userid); //Sets the '?' parameter to the integer 'userid'.
				
				ArrayList<Account> resultList = null;
				statement.execute(); //If query is successful, create list of accounts.
				resultList = new ArrayList<Account>();
				ResultSet results = statement.getResultSet();
				while (results.next()){ //Fetch accountnames from results and add to resultlist.
					resultList.add(new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9)));
				}
				
				return resultList;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch (SQLException e) {
					//Should never be thrown.
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database and returns the account with the given account id and data associated with it.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account getAccount(int accountId) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(
						"SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
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
				
				return account;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database and returns a list of users.
	 * Note: For now the user objects returned by this method only return the 'userId' and 'username'. No other information is received.
	 * @return ArrayList containing all current database users. Returns null if database query fails.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static ArrayList<User> getUsers(int offset) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				//"SELECT USER_ID, CPR, NAME " + "FROM DTUGRP07.USERS;"
				statement = connection.prepareStatement(
								"SELECT *"
								+" FROM ("
								+" SELECT USER_ID, CPR, NAME, ROW_NUMBER() OVER(ORDER BY NAME) AS rownumber"
								+" FROM DTUGRP07.USERS"
								+") AS xxx"
								+" WHERE rownumber BETWEEN ? AND ? ORDER BY NAME;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, offset);
				statement.setInt(2, offset+10);
				
				ArrayList<User> resultList = null;
				statement.execute(); //Perform query, create list of users.
				resultList = new ArrayList<User>();
				ResultSet results = statement.getResultSet();
				while (results.next()){ //Fetch usernames from results and add to resultlist.
					resultList.add(new User(results.getInt(1), results.getString(2), results.getString(3)));
				}
				
				return resultList;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the user with the given userId and all information related to them.
	 * @return User object containing all fields except for 'accounts'. Returns null if database query fails or returns no rows.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static User getUser(int userId) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
						+ "FROM DTUGRP07.USERS "
						+ "WHERE USER_ID = ? "
						+ "FETCH FIRST 1 ROWS ONLY;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, userId);
				
				User user = null;
				statement.execute(); //If query is successful, attempt to create user.
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
				}
				
				return user;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the user with the given cpr-number and all information related to them.
	 * @return User object containing all fields except for 'accounts'. Returns null if database query fails or returns no rows.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static User getUserByCpr(String cpr) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
						+ "FROM DTUGRP07.USERS "
						+ "WHERE CPR = ? "
						+ "FETCH FIRST 1 ROWS ONLY;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, cpr);
				
				User user = null;
				statement.execute(); //If query is successful, attempt to create user.
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
				}
				
				return user;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database for a specific account with the given account number. 
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account getAccountByNumber(String number) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
						+ "FROM DTUGRP07.ACCOUNTS "
						+ "WHERE NUMBER = ? "
						+ "FETCH FIRST 1 ROWS ONLY;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, number);
				
				Account account = null;
				statement.execute(); //If query is successful, attempt to create account object.
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					account = new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9));
				}
				
				return account;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@Deprecated
	public static ArrayList<Transaction> getArchive(int account_id) throws DatabaseException {
		for(int tries = 2; 0 < tries; tries--) {
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT TRANSACTION_ID, ACCOUNT_ID, DATE, DESCRIPTION, AMOUNT FROM DTUGRP07.ARCHIVE "
						+ "WHERE ACCOUNT_ID = ?;" 
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, account_id);
				
				ArrayList<Transaction> resultList = null;
				statement.execute();
				resultList = new ArrayList<Transaction>();
				ResultSet results = statement.getResultSet();
				while(results.next()) {
					resultList.add(new Transaction(results.getLong(1), results.getInt(2), results.getTimestamp(3), results.getString(4), results.getDouble(5)));
				}
				
				return resultList;
				
			} catch (SQLException e) {
				handleSQLException(e, tries);
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static ArrayList<Message> getMessages(int userId) throws DatabaseException {
		for(int tries = 2; 0 < tries; tries--) {
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT MESSAGE, DATE, SENDER_NAME, USER_ID FROM DTUGRP07.INBOX "
						+ "WHERE USER_ID = ? "
						+ "ORDER BY DATE DESC;" 
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, userId);
				
				ArrayList<Message> resultList = null;
				statement.execute();
				resultList = new ArrayList<Message>();
				ResultSet results = statement.getResultSet();
				while(results.next()) {
					resultList.add(new Message(results.getString(1), results.getDate(2), results.getString(3), results.getInt(4)));
				}
				
				//Sorts all Messages by Date.
				//Collections.sort(resultList, messageComparator);
				
				return resultList;
				
			} catch (SQLException e) {
				handleSQLException(e, tries);
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	
		return null;
	}

	/**
	 * Gets a list of all currencies available in the database.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static ArrayList<String> getCurrencies() throws DatabaseException{
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT * "
						+ "FROM DTUGRP07.CURRENCY;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				
				ArrayList<String> currencies = new ArrayList<String>();
				statement.execute(); //If query is successful, attempt to create account object.
				ResultSet results = statement.getResultSet();
				while (results.next()){ //Fetch row if able.
					currencies.add(results.getString(1));
				}
				
				return currencies;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
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
	 * If the 'TransBy.ACCOUNTNUMBER' or 'TransBy.IBAN' method is used, then it is possible that the receiver doesn't exist in the database, if the receiver isn't found, then no transaction is created for the receiver, but the transaction is still completed for the sender.
	 * @param transBy TransBy, defines the method the create the transaction, defines what 'receiverId' contains.
	 * @param senderId The accountId for the account sending this transfer.
	 * @param receiverId The accountId, account-number or IBAN for the account receiving this transfer. Should match the 'transBy' parameter.
	 * @param senderDescription The string with the description of the transfer for the sender.
	 * @param receiverDescription The string with the description of the transfer for the receiver.
	 * @param senderAmount The amount to enter into the transaction for the sender.
	 * @param receiverAmount The amount to enter into the transaction for the receiver.
	 * @return The new transaction for the sender as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account createTransaction(TransBy transBy, int senderId, String receiverId, String senderDescription, String receiverDescription, double senderAmount, double receiverAmount) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			CallableStatement getBalanceStatement = null;
			try {
				Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());
				
				getBalanceStatement = connection.prepareCall("{call DTUGRP07.createTransaction(?,?,?,?,?,?,?,?)}");

				getBalanceStatement.setInt(1,senderId);
				getBalanceStatement.setString(2, receiverId);	
				getBalanceStatement.setString(3,senderDescription);
				getBalanceStatement.setString(4, receiverDescription);
				getBalanceStatement.setDouble(5, senderAmount);
				getBalanceStatement.setDouble(6, receiverAmount);
				getBalanceStatement.setTimestamp(7, date);
				getBalanceStatement.setString(8, transBy.toString());
				getBalanceStatement.execute();

				return DB.getAccount(senderId);
			} catch (SQLException e) {
				handleSQLException(e, tries);
			} finally {
				try {
					if (getBalanceStatement != null) getBalanceStatement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database to insert a new transaction into the TRANSACTIONS table.
	 * Note that no change or checks are made to any accounts with this method.
	 * Uses the 'ID' method to create the transaction as the 'ACCOUNTNUMBER' and 'IBAN' method allow the account to not exist and this method does not allow this.
	 * @param accountId The accountId for the account to have the transaction.
	 * @param description The string with the description of the transaction.
	 * @param amount The amount to enter into the transaction, can be negative.
	 * @return The new transaction as a Transaction object with all fields, excluding 'transaction_id', if successfully created.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account createTransaction(int accountId, String description, double amount) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			CallableStatement statement = null;
			try {
				Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());

				statement = connection.prepareCall("{call DTUGRP07.createTransactionOne(?,?,?,?)}");
				statement.setInt(1, accountId);
				statement.setString(2, description);
				statement.setDouble(3, amount);
				statement.setTimestamp(4, date);
				statement.execute();

				return DB.getAccount(accountId);
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void archiveTransactions() throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			CallableStatement archive = null;
			try {
				archive = connection.prepareCall("{call DTUGRP07.archiveProc()}");
				archive.execute();
				
				return;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (archive != null) archive.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public static boolean createMessage(String message, int senderID, String receiver, TransBy transBy) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			CallableStatement statement = null;
			try {
				Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());
				statement = connection.prepareCall("{CALL DTUGRP07.CREATEMESSAGE(?,?,?,?,?)}");
				
				statement.setString(1, message);
				statement.setTimestamp(2, date);
				statement.setInt(3, senderID);
				statement.setString(4, receiver);
				statement.setString(5, transBy.toString());
				statement.execute();
				
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return false;
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
	 * @throws DatabaseException If a database error occurs.
	 */
	@Deprecated
	public static Transaction createDeposit(int accountId, String description, double amount) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());
				
				statement = connection.prepareStatement("INSERT INTO DTUGRP07.TRANSACTIONS "
						+ "(ACCOUNT_ID, \"DATE\", DESCRIPTION, AMOUNT) VALUES "
						+ "(?, ?, ?, ?);"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, accountId);
				statement.setTimestamp(2, date);
				statement.setString(3, description);
				statement.setDouble(4, amount);
				statement.execute(); //Attempt to insert new row.
				return new Transaction(null, accountId, date, description, amount);
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
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
	 * @throws DatabaseException If a database error occurs.
	 */
	@Deprecated
	public static Transaction createWithdrawal(int accountId, String description, double amount) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());
				
				statement = connection.prepareStatement("INSERT INTO DTUGRP07.TRANSACTIONS "
						+ "(ACCOUNT_ID, \"DATE\", DESCRIPTION, AMOUNT) VALUES "
						+ "(?, ?, ?, ?);"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, accountId);
				statement.setTimestamp(2, date);
				statement.setString(3, description);
				statement.setDouble(4, -amount);
				statement.execute(); //Attempt to insert new row.
				
				return new Transaction(null, accountId, date, description, -amount);
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database to insert a new account into the ACCOUNTS table, then queries the database to return the newly created row.
	 * @return The new account as an Account object with all fields, if successfully created.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account createAccount(int userId, String name, String type, String number, String iban, String currency, double interest) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(
						"SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
						+ "FROM FINAL TABLE("
						+ "INSERT INTO DTUGRP07.ACCOUNTS "
						+ "(USER_ID, \"TYPE\", NAME, NUMBER, IBAN, INTEREST, BALANCE, CURRENCY) VALUES "
						+ "(?, ?, ?, ?, ?, ?, ?, ?)"
						+ ");"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, userId);
				statement.setString(2, type);
				statement.setString(3, name);
				statement.setString(4, number);
				statement.setString(5, iban);
				statement.setDouble(6, interest);
				statement.setDouble(7, 0); //Balance
				statement.setString(8, currency);
				
				statement.execute(); //Attempt to insert new row.
				Account account = null;
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					account = new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9));
				}
				return account;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database to insert a new user into the USERS table, then queries the database to return the newly created row.
	 * @return The new user as a User object with all fields, if successfully created.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static User createUser(String username, String cpr, String name, String institute, String consultant) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(
						"SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
						+ "FROM FINAL TABLE("
						+ "INSERT INTO DTUGRP07.USERS(USERNAME, PASSWORD, CPR, NAME, INSTITUTE, CONSULTANT) "
						+ "VALUES(?, ?, ?, ?, ?, ?)"
						+ ");"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, username);
				statement.setString(2, generatePassword());
				statement.setString(3, cpr);
				statement.setString(4, name);
				statement.setString(5, institute);
				statement.setString(6, consultant);
				
				statement.execute(); //Attempt to insert new row.
				User user = null;
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
				}
				return user;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch (SQLException e) {
					// Should never be thrown.
					e.printStackTrace();
				}
			}
		}
		return null;
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
	 * @throws DatabaseException If a database error occurs.
	 */
	public static User updateUser(int userId, String value, USER attribute) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(
						"SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
						+ "FROM FINAL TABLE("
						+ "UPDATE DTUGRP07.USERS "
						+ "SET "+ attribute.toString() +" = ? "
						+ "WHERE USER_ID = ?"
						+ ");"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, value);
				statement.setInt(2, userId);
				
				statement.execute(); //Attempt to insert new row.
				User user = null;
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
				}
				
				return user;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database to update a user's information for the user with the given user id.
	 * @return True if operation was successful.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static User updateUser(int userId, String username, String cpr, String name, String institute, String consultant) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(
						"SELECT USER_ID, USERNAME, CPR, NAME, INSTITUTE, CONSULTANT "
						+ "FROM FINAL TABLE("
						+ "UPDATE DTUGRP07.USERS "
						+ "SET USERNAME = ?, CPR = ?, NAME = ?, INSTITUTE = ?, CONSULTANT = ? "
						+ "WHERE USER_ID = ?"
						+ ");"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, username);
				statement.setString(2, cpr);
				statement.setString(3, name);
				statement.setString(4, institute);
				statement.setString(5, consultant);
				statement.setInt(6, userId);
				
				statement.execute(); //Attempt to insert new row.
				User user = null;
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					user = new User(results.getInt(1), results.getString(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6));
				}
				
				return user;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	public static boolean resetPassword(int userId) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("UPDATE DTUGRP07.USERS "
						+ "SET PASSWORD = ? "
						+ "WHERE USER_ID = ?;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, generatePassword());
				statement.setInt(2, userId);
				
				statement.execute(); //Attempt to insert new row.
				
				return true;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return false;
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
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account updateAccount(int accountId, String value, ACCOUNT attribute) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement(
						"SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
						+ "FROM FINAL TABLE("
						+ "UPDATE DTUGRP07.ACCOUNTS "
						+ "SET "+ attribute.toString() +" = ? "
						+ "WHERE ACCOUNT_ID = ?"
						+ ");"
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
				Account account = null;
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					account = new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9));
				}
				
				return account;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database to update account information for the account with the given account id.
	 * @return True if operation was successful.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static Account updateAccount(int accountId, String name, String type, String number, String iban, double interest) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			try {
				PreparedStatement statement = connection.prepareStatement(
						"SELECT USER_ID, ACCOUNT_ID, NAME, TYPE, NUMBER, IBAN, CURRENCY, INTEREST, BALANCE "
						+ "FROM FINAL TABLE("
						+ "UPDATE DTUGRP07.ACCOUNTS "
						+ "SET NAME = ?, TYPE = ?, NUMBER = ?, IBAN = ?, INTEREST = ? "
						+ "WHERE ACCOUNT_ID = ?"
						+ ");"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				
				statement.setString(1, name);
				statement.setString(2, type);
				statement.setString(3, number);
				statement.setString(4, iban);
				statement.setDouble(5, interest);
				statement.setInt(6, accountId);
				
				statement.execute(); //Attempt to insert new row.
				Account account = null;
				ResultSet results = statement.getResultSet();
				if (results.next()){ //Fetch row if able.
					account = new Account(results.getInt(1), results.getInt(2), results.getString(3), results.getString(4), results.getString(5), results.getString(6), results.getString(7), results.getDouble(8), results.getDouble(9));
				}
				
				return account;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			}
		}
		return null;
	}
	
	
	// DELETE Methods
	/**
	 * Queries the database to delete the account and any transaction associated with this account with the given account-id.
	 * @return True if operation was successful.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static boolean deleteAccount(int accountId) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.ACCOUNTS "
						+ "WHERE ACCOUNT_ID = ?;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, accountId);
				
				statement.execute(); //Attempt to delete row.
				statement.close();
				return true;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			}
		}
		return false;
	}
	
	public static boolean deleteAccountWithTransfer(int senderId, int receiverId, String receiverDescription, double amount) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			try {
				try {
					connection.setAutoCommit(false);
					DB.createTransaction(receiverId, receiverDescription, amount);
					DB.deleteAccount(senderId);
					return true;
				} catch (Exception e){
					//Error, rollback all changes.
					connection.rollback();
					throw e;
				} finally {
					connection.setAutoCommit(true);
				}
			} catch (SQLException e) {
				handleSQLException(e, tries);
			}
		}
		return false;
	}
	
	/**
	 * Queries the database to delete the account and any transaction associated with this account with the given account-number.
	 * @return True if operation was successful.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static boolean deleteAccountByNumber(String number) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.ACCOUNTS "
						+ "WHERE NUMBER = ?;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, number);
				
				statement.execute(); //Attempt to delete row.
				statement.close();
				return true;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			}
		}
		return false;
	}
	
	/**
	 * Queries the database to delete the user with the given id.
	 * @return True if operation was successful.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static boolean deleteUser(int userId) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			try {
				PreparedStatement statement = connection.prepareStatement("DELETE FROM DTUGRP07.USERS "
						+ "WHERE USER_ID = ?;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setInt(1, userId);
				
				statement.execute(); //Attempt to delete row.
				statement.close();
				return true;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			}
		}
		return false;
	}
	
	public static ArrayList<User> searchUsers(String nameOrCpr) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--) {
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("SELECT USER_ID, NAME, CPR " +
						"FROM DTUGRP07.USERS WHERE NAME LIKE ? OR CPR LIKE ?;");
				nameOrCpr.toLowerCase();
				statement.setString(1, "%"+nameOrCpr+"%");
				statement.setString(2, "%"+nameOrCpr+"%");
				ArrayList<User> resultList = null;
				statement.execute();
				resultList = new ArrayList<User>();
				ResultSet results = statement.getResultSet();
				while(results.next()) {
					resultList.add(new User(results.getInt(1), results.getString(3), results.getString(2)));
				}
				
				return resultList;
			} catch (SQLException e) {
				handleSQLException(e, tries);
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	//Change date to timestamp!! when db works
	public static ArrayList<Transaction> searchArchive(int userID, long from, long to) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--) {
			PreparedStatement statement = null;
			try {
				Timestamp dateFrom = new Timestamp(from);
				Timestamp dateTo = new Timestamp(to);
				statement = connection.prepareStatement(
						"SELECT TRANSACTION_ID, DTUGRP07.ARCHIVE.ACCOUNT_ID, DESCRIPTION, DATE, AMOUNT "
						+ "FROM DTUGRP07.ARCHIVE "
						+ "LEFT OUTER JOIN DTUGRP07.ACCOUNTS ON DTUGRP07.ARCHIVE.ACCOUNT_ID=DTUGRP07.ACCOUNTS.ACCOUNT_ID "
						+ "WHERE DTUGRP07.ACCOUNTS.USER_ID = ? AND DATE > ? AND DATE < ? "
						+ "ORDER BY DATE DESC;");
				statement.setInt(1, userID);
				statement.setTimestamp(2, dateFrom);
				statement.setTimestamp(3, dateTo);
				ArrayList<Transaction> resultList = null;
				statement.execute();
				resultList = new ArrayList<Transaction>();
				ResultSet results = statement.getResultSet();
				while(results.next()) {
					resultList.add(new Transaction(results.getLong(1), results.getInt(2), results.getTimestamp(4), results.getString(3), results.getInt(5)));
				}
				
				//Collections.sort(resultList, transactionComparator);				
				return resultList;
			} catch (SQLException e) {
				handleSQLException(e, tries);
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	/**
	 * Queries the database to delete the user with the given cpr-number.
	 * @return True if operation was successful.
	 * @throws DatabaseException If a database error occurs.
	 */
	public static boolean deleteUserByCpr(String cpr) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			PreparedStatement statement = null;
			try {
				statement = connection.prepareStatement("DELETE FROM DTUGRP07.USERS "
						+ "WHERE CPR = ?;"
						, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				statement.setString(1, cpr);
				
				statement.execute(); //Attempt to delete row.
				
				return true;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (statement != null) statement.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	
	// GENERAL Methods
	/**
	 * Queries a request for user information and checks for correct password. Returns the 'userId' of the user only if user exists and correct password is given as argument, else returns -1.
	 * Uses a PreparedStatement to query.
	 * @throws DatabaseException If a database error occurs.
	 */
	
	public static int checkLogin(String username, String password) throws DatabaseException {
		for (int tries = 2; 0 < tries; tries--){
			CallableStatement check = null;
			try {
				check = connection.prepareCall("{call DTUGRP07.checkLogin(?,?,?)}",
						ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				check.setString(1,username);
				check.setString(2, password);
				check.registerOutParameter(3, java.sql.Types.INTEGER);
				check.execute();
				int result = check.getInt(3);
				
				return result;
			} catch (SQLException e) {
				handleSQLException(e, tries);
				//if no more tries, throw exception.
			} finally {
				try {
					if (check != null) check.close();
				} catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
		return -1;
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
			} else {
				try {
					connection.close();
				} catch (SQLException e){}
			}
			connection = DriverManager.getConnection(url, getProperties());
		}
	}
	
	public static void getConnection2() throws SQLException {
		if (connection != null)
			try {
				connection.close();
			} catch(SQLException e){}
		connection = ds1.getConnection();
	}
	
	public static void setConnection(Connection connection){
		if (DB.connection != null)
			try {
				DB.connection.close();
			} catch(SQLException e){}
		DB.connection = connection;
	}
	
	public static void close(){
		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
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
		properties.put("retrieveMessagesFromServerOnGetMessage", "true");
		properties.put("emulateParameterMetaDataForZCalls", "1");
		return properties;
	}
	
	/**
	 * Handle SQLException, possibly try to fix the problem or if not possible, throw exception.
	 * @param tries The number of times to try and fix the exception before it should be thrown.
	 * @return Returns true if a fix was deployed and a retry could possibly be successful (Not necessarily, beware infinite loop!), false if no fix was deployed.
	 * @throws DatabaseException If an irreversible SQLException has happened.
	 */
	private static boolean handleSQLException(SQLException e, int tries) throws DatabaseException{
		
		if (tries <= 1) throw new DatabaseException(e.getMessage(), e.getErrorCode(), e.getSQLState());
		
		switch (e.getSQLState()){
		case "01002": //disconnect error
		case "08000": //connection exception
		case "08001": //SQL client unable to establish SQL connection
		case "08002": //connection name in use
		case "08003": //connection does not exist
		case "08004": //SQL server rejected SQL connection
		case "08006": //connection failure
		case "82119": //connect error; can't get error text
		case "69000": //SQL*Connect errors
		case "82117": //invalid OPEN or PREPARE for this connection
		case "82118": //application context not found
			try {
				getConnection(true);
			} catch (SQLException e1) {
				throw new DatabaseException(e.getMessage(), e.getErrorCode(), e.getSQLState());
			}
			return true;
		case "2E000": //invalid connection name
		case "40003": //statement completion unknown
		case "08007": //transaction resolution unknown
		case "2D000": //invalid transaction termination
		case "08008": //insufficient funds
		default:
			throw new DatabaseException(e.getMessage(), e.getErrorCode(), e.getSQLState());
		}
	}

	/**
	 * Generates a (possibly) pseudo-random string for use as a password.
	 * @return A new password as a string.
	 */
	private static String generatePassword() {
		String password = "password";
		//Could randomly generate a password.
		return password;
	}
}
