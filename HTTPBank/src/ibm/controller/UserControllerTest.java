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

public class UserControllerTest {
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
	
	/*@Test
	public void testDeleteUser() throws ServletException, IOException, DatabaseException {
		
		String username = "Thomas";
		session.setAttribute("user", DB.getUser(1));
		mockThatServlet.setUrlPattern("/admin/deleteUser");
		
		new UserController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "users");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully deleted user: " + username);

		System.out.println(mockThatServlet.printInfo());	
		
	}*/

	@Test
	public void testNewUser() throws ServletException, IOException, DatabaseException {
		
		String username = "Thomas";
		session.setAttribute("user", DB.getUser(1));
		mockThatServlet.putParameter("username", username);
		mockThatServlet.putParameter("cpr", "1234567899");
		mockThatServlet.putParameter("name", "Lenny");
		mockThatServlet.putParameter("institute", "Bornholm");
		mockThatServlet.putParameter("consultant", "Lennart");
		
		mockThatServlet.setUrlPattern("/admin/newUser");
		
		new UserController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "users");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully created new user: " + username);

		System.out.println(mockThatServlet.printInfo());	
		
	}
	
}
