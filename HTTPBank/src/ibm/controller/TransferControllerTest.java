package ibm.controller;
import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.DatabaseException;
import ibm.test.MockThatServlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class TransferControllerTest {
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
	public void testAdminInternationalTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/doTransfer");
		
		//to is IBAN number
		String to = "DK00002";
		
		mockThatServlet.putParameter("id", "3");
		mockThatServlet.putParameter("from", "3");
		mockThatServlet.putParameter("to", to);
		mockThatServlet.putParameter("bic", "DKDKDKDK");
		mockThatServlet.putParameter("amount", "2.00");
		mockThatServlet.putParameter("change", "1.00");
		mockThatServlet.putParameter("message", "Test message");
		mockThatServlet.putParameter("international", "true");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "international");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + to + " completed successfully and message sent.");

		System.out.println(mockThatServlet.printInfo());

		
	}
	
	@Test
	public void testUserInternationalTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/user/doTransfer");
		
		//to is IBAN number
		String to = "DK00002";
		
		mockThatServlet.putParameter("id", "3");
		mockThatServlet.putParameter("from", "3");
		mockThatServlet.putParameter("to", to);
		mockThatServlet.putParameter("bic", "DKDKDKDK");
		mockThatServlet.putParameter("amount", "2.00");
		mockThatServlet.putParameter("change", "1.00");
		mockThatServlet.putParameter("message", "Test message");
		mockThatServlet.putParameter("international", "true");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "international");
		
		//assertNotNull(session.getAttribute("exception"));
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + to + " completed successfully and message sent.");

		System.out.println(mockThatServlet.printInfo());

		
	}
	
	@Test
	public void testAdminDomesticTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/doTransfer");
		
		String to = "999999999";
		
		mockThatServlet.putParameter("id", "3");
		mockThatServlet.putParameter("from", "3");
		mockThatServlet.putParameter("to", to);
		mockThatServlet.putParameter("amount", "3.00");
		mockThatServlet.putParameter("change", "3.00");
		mockThatServlet.putParameter("message", "Test message");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "transfer");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + to + " completed successfully and message sent.");

		System.out.println(mockThatServlet.printInfo());

		
	}
	
	@Test
	public void testUserDomesticTransfer() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/user/doTransfer");
		
		String to = "999999999";
		
		mockThatServlet.putParameter("id", "3");
		mockThatServlet.putParameter("from", "3");
		mockThatServlet.putParameter("to", to);
		mockThatServlet.putParameter("amount", "1.00");
		mockThatServlet.putParameter("change", "1.00");
		mockThatServlet.putParameter("message", "Test message");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "transfer");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Transfer to " + to + " completed successfully and message sent.");

		System.out.println(mockThatServlet.printInfo());

		
	}
	
	@Test
	public void testTransferFailed() throws ServletException, IOException, DatabaseException {
		mockThatServlet.setUrlPattern("/admin/doTransfer");
		
		//to is a non-existing IBAN number
		String to = "DKK0002";
		
		mockThatServlet.putParameter("id", "3");
		mockThatServlet.putParameter("from", "3");
		mockThatServlet.putParameter("to", to);
		mockThatServlet.putParameter("bic", "DKDKDKDK");
		mockThatServlet.putParameter("amount", "2.00");
		mockThatServlet.putParameter("change", "1");
		mockThatServlet.putParameter("message", "Test message");
		mockThatServlet.putParameter("international", "true");
		
		new TransferController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "international");
		
		assertNotNull(session.getAttribute("errors"));
		
		System.out.println(mockThatServlet.printInfo());

		
	}
	
	
	
	
}
