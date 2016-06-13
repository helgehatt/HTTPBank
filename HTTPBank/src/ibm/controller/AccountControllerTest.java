package ibm.controller;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.DatabaseException;
import ibm.test.MockThatServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class AccountControllerTest {
	MockThatServlet mockThatServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;

	@Before
	public void setUp() throws IOException {
		mockThatServlet = new MockThatServlet();
		request = mockThatServlet.getRequest();
		response = mockThatServlet.getResponse();
		session = request.getSession();
	}

	@Test
	public void testNewAccount() throws ServletException, IOException, DatabaseException {
		//Note this expects the database to be up...
		session.setAttribute("user", DB.getUser(1));
		
		String number = "123456789";
		
		mockThatServlet.putParameter("name", "The Account Name");
		mockThatServlet.putParameter("type", "The Account Type");
		mockThatServlet.putParameter("number", number);
		mockThatServlet.putParameter("iban", "IB12345678");
		mockThatServlet.putParameter("currency", "DKK");
		mockThatServlet.putParameter("interest", "0.02");
		mockThatServlet.putParameter("balance", "0");
		
		mockThatServlet.setUrlPattern("/admin/newAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully created new account: " + number);

		System.out.println(mockThatServlet.printInfo());
	}
	
	@Test

	public void testNewAccountFailed() throws ServletException, IOException, DatabaseException {
		//Note this expects the database to be up...
		session.setAttribute("user", DB.getUser(1));
		
		String number = "1234589";
		
		mockThatServlet.putParameter("name", "The Acc&%#¤");
		mockThatServlet.putParameter("type", "The Accoun#¤pe");
		mockThatServlet.putParameter("number", number);
		mockThatServlet.putParameter("iban", "IBsnsns12345678");
		mockThatServlet.putParameter("currency", "DKR");
		mockThatServlet.putParameter("interest", "ABC");
		mockThatServlet.putParameter("balance", "0PT");
		
		mockThatServlet.setUrlPattern("/admin/newAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "newaccount");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Failed to create the account: " + number);

		System.out.println(mockThatServlet.printInfo());
	}
	
	@Test
	public void testEditAccount() throws ServletException, IOException, DatabaseException {
		//Note this expects the database to be up...
session.setAttribute("user", DB.getUser(1));
		
		String number = "123456789";
		
		mockThatServlet.putParameter("name", "The Account Name");
		mockThatServlet.putParameter("type", "The Account Type");
		mockThatServlet.putParameter("number", number);
		mockThatServlet.putParameter("iban", "IB12345678");
		mockThatServlet.putParameter("currency", "DKK");
		mockThatServlet.putParameter("interest", "0.02");
		mockThatServlet.putParameter("balance", "0");
		
		mockThatServlet.setUrlPattern("/admin/newAccount");
		
		
		mockThatServlet.setUrlPattern("/admin/editAccount");
		
		new AccountController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accountinfo");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully updated account: " + number);

		System.out.println(mockThatServlet.printInfo());
	}
}
