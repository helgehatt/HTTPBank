package ibm.test;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.db.DB.ACCOUNT;
import ibm.db.DB.TransBy;
import ibm.db.DB.USER;
import ibm.resource.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TestDBException extends Mockito {
	//Fields
	private Connection connection;
	String exceptionMessage;
	int errorCode;
	String sqlState;
	
	String username = "Test Testy Test";
	String password = "password";
	String cpr = "123456789";
	String institute = "HTTPBank";
	String consultant = "Tommy";
	
	int userId = 1;
	String name = "Moo";
	String type = "Type";
	String number = "123456789";
	String iban = "DK123456789";
	String currency = "DKK";
	double interest = 0.5;
	
	int accountId = 1;
	String description = "TestDescription";
	double amount = 100;
	
	String message = "TestMessage";
	int senderID = 1;
	String receiver = ""+1;
	TransBy transBy = TransBy.ID;
	
	@AfterClass
	public static void cleanUp() throws SQLException{
		Properties properties = new Properties();
		properties.put("user", "DTU18");
		properties.put("password", "FAGP2016");
		properties.put("retreiveMessagesFromServerOnGetMessage", "true");
		properties.put("emulateParameterMetaDataForZCalls", "1");
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} catch (ClassNotFoundException e) {
			throw new SQLException(e);
		}
		try {
			DB.setConnection(DriverManager.getConnection("jdbc:db2://192.86.32.54:5040/DALLASB", properties));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void testSetup() throws SQLException{
		connection = mock(Connection.class);
		exceptionMessage = "A mocking exception...";
		errorCode = 1337;
		sqlState = "2E000";
		when(connection.prepareStatement(anyString())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		when(connection.prepareStatement(anyString(), anyInt())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		when(connection.prepareStatement(anyString(), anyInt(), anyInt())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		when(connection.prepareStatement(anyString(), anyInt(), anyInt(), anyInt())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		when(connection.prepareCall(anyString())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		when(connection.prepareCall(anyString(), anyInt(), anyInt())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		when(connection.prepareCall(anyString(), anyInt(), anyInt(), anyInt())).thenThrow(new DatabaseException(exceptionMessage, errorCode, sqlState));
		DB.setConnection(connection);
	}
	
	//Tests
	@Test
	public void testCheckLoginException() throws SQLException {
		try {
			DB.checkLogin(username, password);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testArchiveTransactionsException() throws SQLException {
		try {
			DB.archiveTransactions();
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testCreateAccountException() throws SQLException {
		try {
			DB.createAccount(userId, name, type, number, iban, currency, interest);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCreateDepositException() throws SQLException {
		try {
			DB.createDeposit(accountId, description, amount);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testCreateMessageException() throws SQLException {
		try {
			DB.createMessage(message, senderID, receiver, transBy);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testCreateTransactionOneException() throws SQLException {
		try {
			DB.createTransaction(accountId, description, amount, currency);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testCreateTransactionException() throws SQLException {
		try {
			DB.createTransaction(transBy, accountId, receiver, description, description, amount, currency);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testCreateUserException() throws SQLException {
		try {
			DB.createUser(username, cpr, name, institute, consultant);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testCreateWithdrawalException() throws SQLException {
		try {
			DB.createWithdrawal(accountId, description, amount);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testDeleteAccountException() throws SQLException {
		try {
			DB.deleteAccount(accountId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testDeleteAccountNumberException() throws SQLException {
		try {
			DB.deleteAccountByNumber(number);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testDeleteAccountTransferException() throws SQLException {
		try {
			DB.deleteAccountWithTransfer(senderID, accountId, description, amount, currency);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testDeleteUserException() throws SQLException {
		try {
			DB.deleteUser(userId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testDeleteUserCprException() throws SQLException {
		try {
			DB.deleteUserByCpr(cpr);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetAccountException() throws SQLException {
		try {
			DB.getAccount(accountId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetAccountByException() throws SQLException {
		try {
			DB.getAccountBy(transBy, receiver);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetAccountByNumberException() throws SQLException {
		try {
			DB.getAccountByNumber(number);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetAccountsException() throws SQLException {
		try {
			DB.getAccounts(userId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testGetArchiveException() throws SQLException {
		try {
			DB.getArchive(accountId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetCurrenciesException() throws SQLException {
		try {
			DB.getCurrencies();
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetMessagesException() throws SQLException {
		try {
			DB.getMessages(userId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetCurrencyException() throws SQLException {
		try {
			DB.getReceiverCurrency(receiver, transBy);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetTransactionsException() throws SQLException {
		try {
			DB.getTransactions(accountId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetUserException() throws SQLException {
		try {
			DB.getUser(userId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetUserByCprException() throws SQLException {
		try {
			DB.getUserByCpr(cpr);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testGetUsersException() throws SQLException {
		try {
			DB.getUsers(0);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testResetPasswordException() throws SQLException {
		try {
			DB.resetPassword(userId);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testSearchArchiveException() throws SQLException {
		try {
			DB.searchArchive(userId, System.currentTimeMillis()-86400000, System.currentTimeMillis()+86400000);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testSearchUsersException() throws SQLException {
		try {
			DB.searchUsers(name);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
		try {
			DB.searchUsers(cpr);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testUpdateAccountException() throws SQLException {
		try {
			DB.updateAccount(accountId, iban, ACCOUNT.IBAN);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testUpdateAccountAllException() throws SQLException {
		try {
			DB.updateAccount(accountId, name, type, number, iban, interest);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testUpdateUserException() throws SQLException {
		try {
			DB.updateUser(userId, consultant, USER.CONSULTANT);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
	
	@Test
	public void testUpdateUserAllException() throws SQLException {
		try {
			DB.updateUser(userId, username, cpr, name, institute, consultant);
			fail("Should throw exception.");
		} catch(DatabaseException e){
			assertEquals(exceptionMessage, e.getMessage());
			assertEquals(errorCode, e.getErrorCode());
			assertEquals(sqlState, e.getSQLState());
		}
	}
}
