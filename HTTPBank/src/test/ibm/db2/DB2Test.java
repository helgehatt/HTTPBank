package test.ibm.db2;

import ibm.db.DB;
import ibm.resource.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class made for testing the database integration.
 * @author Tengux
 */
public class DB2Test {
	//Main
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		ArrayList<User> login = DB.getUsers();
		System.out.println(login.isEmpty());
		/*DB2Test db2 = new DB2Test();
		PreparedStatement statement = db2.prepareStatement("SELECT * FROM DTUGRP07.SAMPLE");
		ResultSet results = db2.executeQuery(statement);
		ResultSetMetaData metadata = results.getMetaData();
		int numOfColumns = metadata.getColumnCount();
		for (int i = 1; i <= numOfColumns; i++)
			System.out.print(metadata.getColumnName(i)+" ");
		System.out.println();
		while (results.next()){
			for (int i = 1; i <= numOfColumns; i++)
				System.out.print(results.getString(i)+" ");
			System.out.println();
		}*/
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
	private PreparedStatement prepareStatement(String string) throws SQLException {
		return connection.prepareStatement(string);
	}
	
	private ResultSet executeQuery(PreparedStatement statement) throws SQLException {
		return statement.executeQuery();
	}
	
	@SuppressWarnings("unused")
	private ResultSet executeQuery(String statement) throws SQLException {
		return connection.createStatement().executeQuery(statement);
	}
	
}
