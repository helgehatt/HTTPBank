package ibm.test;

import ibm.db.DB;
import ibm.resource.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

public class DB2Test {
	//Main
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		long start = System.currentTimeMillis();
		System.out.println("Checklogin as Thomas: " + DB.checkLogin("Thomas", "1234"));
		System.out.println("Query Time: " + (System.currentTimeMillis()-start) +System.lineSeparator());
		
		start = System.currentTimeMillis();
		ArrayList<User> users = DB.getUsers();
		System.out.println("Got Users, Query Time: " + (System.currentTimeMillis()-start));
		for (User user : users)
			System.out.println(user.getId()+" : "+user.getUsername());
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
