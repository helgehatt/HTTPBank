package ibm.controller;
import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.db.DB.ACCOUNT;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.User;
import ibm.test.MockThatServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TransferControllerTest {
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
		String number = "43211234";
		String iban = "IB12345678";
		String currency = "DKK";
		double interest = 0.05;
		testAccount1 = DB.createAccount(userId, accountName+1, type, number+1, iban+1, currency, interest);
		testAccount2 = DB.createAccount(userId, accountName+2, type, number+2, iban+2, currency, interest);
		DB.updateAccount(testAccount1.getId(), ""+1000.0, ACCOUNT.BALANCE);
	}
	
	double amount = 100.0;
	String message = "TestDescription";

	@Test
	public void testAdminInternationalTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/doTransfer");
		
		mockThatServlet.putParameter("international", "true");
		mockThatServlet.putParameter("from-id", ""+testAccount1.getId());
		mockThatServlet.putParameter("from-number", testAccount1.getNumber());
		mockThatServlet.putParameter("to-number", testAccount2.getIban());
		mockThatServlet.putParameter("input-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-currency", testAccount1.getCurrency());
		mockThatServlet.putParameter("message", message);
		mockThatServlet.putParameter("to-bic", "HTTPBANK");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "international");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + testAccount2.getIban() + " completed successfully and message sent.");
	}
	
	@Test
	public void testUserInternationalTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/user/doTransfer");
		
		mockThatServlet.putParameter("international", "true");
		mockThatServlet.putParameter("from-id", ""+testAccount1.getId());
		mockThatServlet.putParameter("from-number", testAccount1.getNumber());
		mockThatServlet.putParameter("to-number", testAccount2.getIban());
		mockThatServlet.putParameter("input-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-currency", testAccount1.getCurrency());
		mockThatServlet.putParameter("message", message);
		mockThatServlet.putParameter("to-bic", "HTTPBANK");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "international");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + testAccount2.getIban() + " completed successfully and message sent.");
	}
	
	@Test
	public void testAdminDomesticTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/doTransfer");
		
		mockThatServlet.putParameter("from-id", ""+testAccount1.getId());
		mockThatServlet.putParameter("from-number", testAccount1.getNumber());
		mockThatServlet.putParameter("to-number", testAccount2.getNumber());
		mockThatServlet.putParameter("input-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-currency", testAccount1.getCurrency());
		mockThatServlet.putParameter("message", message);
		mockThatServlet.putParameter("to-bic", "HTTPBANK");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "transfer");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + testAccount2.getNumber() + " completed successfully and message sent.");
	}
	
	@Test
	public void testUserDomesticTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/user/doTransfer");
		
		mockThatServlet.putParameter("from-id", ""+testAccount1.getId());
		mockThatServlet.putParameter("from-number", testAccount1.getNumber());
		mockThatServlet.putParameter("to-number", testAccount2.getNumber());
		mockThatServlet.putParameter("input-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-currency", testAccount1.getCurrency());
		mockThatServlet.putParameter("message", message);
		mockThatServlet.putParameter("to-bic", "HTTPBANK");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "transfer");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + testAccount2.getNumber() + " completed successfully and message sent.");
	}
	
	@Test
	public void testTransferFailed() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/doTransfer");
		
		mockThatServlet.putParameter("from-id", ""+testAccount1.getId());
		mockThatServlet.putParameter("from-number", testAccount1.getNumber());
		mockThatServlet.putParameter("to-number", testAccount2.getNumber()+"MOO");
		mockThatServlet.putParameter("input-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-amount", ""+amount);
		mockThatServlet.putParameter("withdrawn-currency", testAccount1.getCurrency());
		mockThatServlet.putParameter("message", message);
		mockThatServlet.putParameter("to-bic", "HTTPBANK");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals("transfer", mockThatServlet.getRedirectedTo());
		
		assertNotNull(session.getAttribute("errors"));
	}
	
	
	
	
}
