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
		
		String number = "123456789";
		
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
	
	@Test
	public void testCloseAccountWithTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/closeAccount");
		
		String number = "123456789";
		
		mockThatServlet.putParameter("type", "transfer");
		mockThatServlet.putParameter("amount", "1000");
		mockThatServlet.putParameter("to", "1");
		session.setAttribute("account", DB.getAccountByNumber(number));
		
		new AccountCloser().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully closed account: " + number);

		System.out.println(mockThatServlet.printInfo());
	}
	
	@Test
	public void testCloseAccountWithTransferIdFailed() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/closeAccount");
		
		String number = "123456789";
		
		mockThatServlet.putParameter("type", "transfer");
		mockThatServlet.putParameter("amount", "1000");
		//Enter receiverId that is not a number
		mockThatServlet.putParameter("to", "1DK23");
		session.setAttribute("account", DB.getAccountByNumber(number));
		
		new AccountCloser().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "accounts");
		
		assertNotNull(session.getAttribute("exception"));
		assertEquals(session.getAttribute("exception"), "Failed to parse the ID.");

		System.out.println(mockThatServlet.printInfo());
	}
	
	
}
