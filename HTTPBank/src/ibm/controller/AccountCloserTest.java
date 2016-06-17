package ibm.controller;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.User;
import ibm.test.MockThatServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountCloserTest {
	MockThatServlet mockThatServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;

	User testUser;
	Account testAccount1;
	Account testAccount2;
		
	@After
	public void cleanUp() throws DatabaseException{
		//Delete Test Account
		if (testAccount1 != null) DB.deleteAccount(testAccount1.getId());
		if (testAccount2 != null) DB.deleteAccount(testAccount2.getId());
		//Delete Test User
		if (testUser != null) DB.deleteUser(testUser.getId());
	}
	
	@Before
	public void setUp() throws IOException, DatabaseException {
		mockThatServlet = new MockThatServlet();
		request = mockThatServlet.getRequest();
		response = mockThatServlet.getResponse();
		session = request.getSession();
		
		//Create Test User
		String username = "TestUser";
		String cpr = "Test00-1234";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		testUser = DB.createUser(username, cpr, userName, institute, consultant);

		//Create Test Account
		int userId = testUser.getId();
		String accountName = "TestAccountIsTest";
		String type = "TestTypeForTestAccount";
		String number = "Test12345";
		String iban = "Test12345IBAN";
		String currency = "DKK";
		double interest = 0.05;
		testAccount1 = DB.createAccount(userId, accountName+1, type, number+1, iban+1, currency, interest);
		testAccount2 = DB.createAccount(userId, accountName+2, type, number+2, iban+2, currency, interest);
	}

	@Test
	public void testCloseAccountNoTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/closeAccount");
		
		mockThatServlet.putParameter("type", "notransfer");
		mockThatServlet.putParameter("amount", "0");
		session.setAttribute("account", testAccount1);
		
		new AccountCloser().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully closed account: " + testAccount1.getNumber());
	}
	
	@Test
	public void testCloseAccountWithTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/closeAccount");
		
		mockThatServlet.putParameter("type", "transfer");
		mockThatServlet.putParameter("amount", "1000");
		mockThatServlet.putParameter("to", ""+testAccount2.getId());
		session.setAttribute("account", testAccount1);
		
		new AccountCloser().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals("Transfer completed and successfully closed account: "+testAccount1.getNumber(), session.getAttribute("confirmation"));
	}
	
	@Test
	public void testCloseAccountWithTransferIdFailed() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/closeAccount");
		
		mockThatServlet.putParameter("type", "transfer");
		mockThatServlet.putParameter("amount", "1000");
		//Enter receiverId that is not a number
		mockThatServlet.putParameter("to", "1DK23");
		session.setAttribute("account", testAccount1);
		
		new AccountCloser().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("exception"));
		assertEquals(session.getAttribute("exception"), "Failed to parse the ID.");
	}
	
	
}
