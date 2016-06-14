package ibm.controller;

import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.DatabaseException;
import ibm.test.MockThatServlet;

import java.io.IOException;
import java.util.HashMap;

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
		
		String number = "Number123";
		
		mockThatServlet.putParameter("name", "The Account Name");
		mockThatServlet.putParameter("type", "The Account Type");
		mockThatServlet.putParameter("number", number);
		mockThatServlet.putParameter("iban", "IB12345678");
		mockThatServlet.putParameter("currency", "DKK");
		mockThatServlet.putParameter("interest", "0.02");
		mockThatServlet.putParameter("balance", "0");
		
		mockThatServlet.setUrlPattern("/admin/newAccount");
		
		new AccountController().doPost(request, response);
		
		HashMap<String, String> map = (HashMap<String, String>) session.getAttribute("errors");
		System.out.println(map.get("name"));
		System.out.println(map.get("type"));
		System.out.println(map.get("number"));
		System.out.println(map.get("iban"));
		System.out.println(map.get("currency"));
		System.out.println(map.get("interest"));
		System.out.println(map.get("balance"));
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully created new account: " + number);

		System.out.println(mockThatServlet.printInfo());
	}
}
