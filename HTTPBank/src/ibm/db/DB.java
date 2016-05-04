package ibm.db;

import ibm.resource.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * A class for handling all Database queires required by the HTTPBank.
 * TODO list:
 * TODO: Read up if PreparedStatement cache is enabled on application server.
 * TODO: Is a connection pool more efficient vs preparedstatements with single connection? Best practice?
 * 
 * Method TODO list:
 * What methods do we need in here?
 */
public class DB {
	//Fields
	private static Connection connection;
	private static final String url = "jdbc:db2://192.86.32.54:5040/DALLASB";
	
	//Methods
	/**
	 * Queries the database and returns a list of accounts related to a specific user-id.
	 * TODO: Should return account objects in a list, but this is not currently possible as a new constructor and structure for the current resource.account object needs to be created.
	 * @return ArrayList containing all current database accounts related to the given user. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<String> getAccounts(int userid) throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT ACCOUNTNAME " //TODO: Should fetch everything.
				+ "FROM DTUGRP07.ACCOUNTS "
				+ "WHERE OWNER = ?;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setInt(1, userid); //Sets the '?' parameter to the integer 'userid'.
		
		ArrayList<String> resultList = null;
		if (statement.execute()){ //If query is successful, create list of accounts.
			resultList = new ArrayList<String>();
			ResultSet results = statement.getResultSet();
			while (results.next()){ //Fetch accountnames from results and add to resultlist.
				resultList.add(results.getString(1));
			}
			results.close();
		}
		statement.close();
		return resultList;
	}
	
	/**
	 * Queries the database and returns a list of users.
	 * TODO: Should return user objects in a list, but this is not currently possible as a new constructor and structure for the current resource.user object needs to be created.
	 * @return ArrayList containing all current database users. Returns null if database query fails.
	 * @throws SQLException {@link #checkConnection() CheckConnection()}
	 */
	public static ArrayList<String> getUsers() throws SQLException{
		checkConnection();
		
		PreparedStatement statement = connection.prepareStatement("SELECT USERID, USERNAME, PASSWORD "
				+ "FROM DTUGRP07.USERS;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		
		ArrayList<String> resultList = null;
		if (statement.execute()){ //If query is successful, create list of users.
			resultList = new ArrayList<String>();
			ResultSet results = statement.getResultSet();
			while (results.next()){ //Fetch usernames from results and add to resultlist.
				resultList.add(results.getString(2));
			}
			results.close();
		}
		statement.close();
		return resultList;
	}
	
	/**
	 * Queries a request for user information and checks for correct password. Returns true only if user exists and correct password is given as argument.
	 * Uses a PreparedStatement to query.
	 * @throws SQLException 
	 */
	public static boolean checkLogin(String username, String password) throws SQLException{
		checkConnection();
		
		if (username.matches("\\s")) return false; //Checks for white space. TODO: Should throw exception?
		PreparedStatement statement = connection.prepareStatement("SELECT USERID, USERNAME, PASSWORD "
				+ "FROM DTUGRP07.USERS "
				+ "WHERE USERNAME = ? "
				+ "FETCH FIRST 1 ROWS ONLY;"
				, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
		statement.setString(1, username); //Sets the '?' parameter in the SQL statement to be the string 'username'.
		
		boolean passwordIsCorrect = false;
		if (statement.execute()){ //Executes the SQL statement, returns false if failed for any reason.
			ResultSet result = statement.getResultSet();
			if (result.next()){ //Moves the cursor to first row, returns false if cursor is moved past the last row. Note: There should be 0 or 1 row.
				String correctPassword = result.getString("PASSWORD");
				
				passwordIsCorrect = password.equals(correctPassword); //Checks if password is correct.
				//TODO: Remember user-id for later queries if correct, since we have it here?
			}
			result.close();
		}
		statement.close();
		return passwordIsCorrect; //Returns false if any of the above if statements returns false.
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
	 * @throws SQLException If connection fails, driver is missing or connection is found to not be valid for a database.
	 */
	private static void checkConnection() throws SQLException {
		if (connection == null){
			try {
	            Class.forName("com.ibm.db2.jcc.DB2Driver");
	        } catch (ClassNotFoundException e) {
	            throw new SQLException(e);
	        }
			connection = DriverManager.getConnection(url, getProperties());
			if (!connection.isValid(5000)){
				connection = null;
				throw new SQLException("Connection is not valid.");
			}
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
