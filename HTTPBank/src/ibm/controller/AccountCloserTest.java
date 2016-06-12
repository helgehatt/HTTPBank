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

public class AccountCloserTest {
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
	public void testCloseAccountNoTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/closeAccount");
		
		String number = "Number1234";
		
		mockThatServlet.putParameter("type", "notransfer");
		mockThatServlet.putParameter("amount", "0");
		session.setAttribute("account", DB.getAccountByNumber(number));
		
		new AccountCloser().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully closed account: " + number);

		System.out.println(mockThatServlet.printInfo());
	}
}
