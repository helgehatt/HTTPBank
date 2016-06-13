package ibm.test;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestDBException extends Mockito {
	//Fields
	private Connection connection;
	String message;
	int errorCode;
	String sqlState;
	
	//Tests
	@Before
	public void testSetup() throws SQLException{
		connection = mock(Connection.class);
		message = "A mocking exception...";
		errorCode = 1337;
		sqlState = "2E000";
		when(connection.prepareStatement(anyString())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareStatement(anyString(), anyInt(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareStatement(anyString(), anyInt(), anyInt(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareCall(anyString())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareCall(anyString(), anyInt(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		when(connection.prepareCall(anyString(), anyInt(), anyInt(), anyInt())).thenThrow(new DatabaseException(message, errorCode, sqlState));
		DB.setConnection(connection);
	}
	
	@Test
	public void testCheckLoginException() throws SQLException {
		//CheckLogin Exception
		try {
			DB.checkLogin("Thomas", "1234");
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(message, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testArchiveTransactionsException() throws SQLException {
		//CheckLogin Exception
		try {
			DB.archiveTransactions();
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(message, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
}
