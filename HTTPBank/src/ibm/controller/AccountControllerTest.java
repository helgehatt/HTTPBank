package ibm.controller;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.User;
import ibm.test.MockThatServlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountControllerTest {
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
		DB.deleteAccountByNumber(number+3);
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
		number = "12345432";
		String iban = "Test12345IBAN";
		String currency = "DKK";
		double interest = 0.05;
		testAccount1 = DB.createAccount(userId, accountName+1, type, number+1, iban+1, currency, interest);
		testAccount2 = DB.createAccount(userId, accountName+2, type, number+2, iban+2, currency, interest);
	}
	
	String number;

	@Test
	public void testNewAccount() throws ServletException, IOException, DatabaseException {
		session.setAttribute("user", testUser);
		
		mockThatServlet.putParameter("name", "The Account Name");
		mockThatServlet.putParameter("type", "The Account Type");
		mockThatServlet.putParameter("number", number+3);
		mockThatServlet.putParameter("iban", "IB12345678");
		mockThatServlet.putParameter("currency", "DKK");
		mockThatServlet.putParameter("interest", "0.02");
		
		mockThatServlet.setUrlPattern("/admin/newAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals("accounts", mockThatServlet.getRedirectedTo());
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals("Successfully created new account: " + number+3, session.getAttribute("confirmation"));
	}
	
	@Test
	public void testNewAccountError() throws ServletException, IOException, DatabaseException {
		session.setAttribute("user", testUser);
		
		String number = "1DK234589";
		
		mockThatServlet.putParameter("name", "The Ac123");
		mockThatServlet.putParameter("type", "The Accou1234");
		mockThatServlet.putParameter("number", number+3);
		mockThatServlet.putParameter("iban", "IBsnsns12345678");
		mockThatServlet.putParameter("currency", "DKR");
		mockThatServlet.putParameter("interest", "ABC");
		mockThatServlet.putParameter("balance", "0PT");
		
		mockThatServlet.setUrlPattern("/admin/newAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals("newaccount", mockThatServlet.getRedirectedTo());
		
		assertNotNull(session.getAttribute("errors"));
	}
	
	@Test
	public void testEditAccount() throws ServletException, IOException, DatabaseException {
		session.setAttribute("account", testAccount1);
		
		mockThatServlet.putParameter("name", "The Edited Account Name");
		mockThatServlet.putParameter("type", "The Edited Account Type");
		mockThatServlet.putParameter("number", number+3);
		mockThatServlet.putParameter("iban", "IB12345378");
		mockThatServlet.putParameter("currency", "DKK");
		mockThatServlet.putParameter("interest", "0.04");
		mockThatServlet.putParameter("balance", "1000");	
		
		mockThatServlet.setUrlPattern("/admin/editAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accountinfo");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully updated account: " + number+3);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testEditAccountError() throws ServletException, IOException, DatabaseException {
		//Note this expects the database to be up...
		session.setAttribute("account", testAccount1);
		
		String number = "DK3456788";
		
		mockThatServlet.putParameter("name", "The Edited Account Name1");
		mockThatServlet.putParameter("type", "The Edited Account Type");
		mockThatServlet.putParameter("number", number+"T");
		mockThatServlet.putParameter("iban", "IBGG12345378");
		mockThatServlet.putParameter("currency", "DKR");
		mockThatServlet.putParameter("interest", "0.A");
		mockThatServlet.putParameter("balance", "1BB");	
		
		mockThatServlet.setUrlPattern("/admin/editAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "editaccount");
		
		assertNotNull(session.getAttribute("errors"));
		assertEquals("Please enter 9 digits." ,((HashMap<String, String>) session.getAttribute("errors")).get("number"));
	}
}
