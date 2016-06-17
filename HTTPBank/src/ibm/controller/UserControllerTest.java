package ibm.controller;
import static org.junit.Assert.*;
import ibm.db.DB;
import ibm.resource.Account;
import ibm.resource.DatabaseException;
import ibm.resource.User;
import ibm.test.MockThatServlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserControllerTest {
	MockThatServlet mockThatServlet;
	HttpServletRequest request;
	HttpServletResponse response;
	HttpSession session;

	User testUser;
		
	@After
	public void cleanUp() throws DatabaseException{
		//Delete Test Users
		DB.deleteUserByCpr(cpr+1);
		DB.deleteUserByCpr(cpr+2);
	}
	
	String username;
	String cpr;
	
	@Before
	public void setUp() throws IOException, DatabaseException {
		mockThatServlet = new MockThatServlet();
		request = mockThatServlet.getRequest();
		response = mockThatServlet.getResponse();
		session = request.getSession();
		
		//Create Test User
		username = "TestUser";
		cpr = "432198-123";
		String userName = "Test Testy Test";
		String institute = "Test That Institute";
		String consultant = "";
		testUser = DB.createUser(username+1, cpr+1, userName, institute, consultant);
	}
	
	@Test
	public void testDeleteUser() throws ServletException, IOException, DatabaseException {
		ArrayList<User> users = new ArrayList<User>();
		users.add(testUser);
		session.setAttribute("users", users);
		session.setAttribute("user", testUser);
		mockThatServlet.setUrlPattern("/admin/deleteUser");
		
		new UserController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "users");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals("Successfully deleted the user: "+testUser.getName(), session.getAttribute("confirmation"));
	}
	

	@Test
	public void testNewUser() throws ServletException, IOException, DatabaseException {
		session.setAttribute("users", new ArrayList<User>());
		session.setAttribute("user", testUser);
		mockThatServlet.putParameter("username", username+2);
		mockThatServlet.putParameter("cpr", cpr+2);
		mockThatServlet.putParameter("name", testUser.getName());
		mockThatServlet.putParameter("institute", testUser.getInstitute());
		mockThatServlet.putParameter("consultant", testUser.getConsultant());
		
		mockThatServlet.setUrlPattern("/admin/newUser");
		
		new UserController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "users");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals(session.getAttribute("confirmation"), "Successfully created new user: " + testUser.getName());
	}
	
	@Test
	public void testNewUserFailed() throws ServletException, IOException, DatabaseException {
		session.setAttribute("user", testUser);
		mockThatServlet.putParameter("username", username+1);
		mockThatServlet.putParameter("cpr", cpr+"MOO");
		mockThatServlet.putParameter("name", testUser.getName());
		mockThatServlet.putParameter("institute", testUser.getInstitute());
		mockThatServlet.putParameter("consultant", testUser.getConsultant());
		
		mockThatServlet.setUrlPattern("/admin/newUser");
		
		new UserController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "newuser");
		
		assertNotNull(session.getAttribute("errors"));
	}
	
	@Test
	public void testEditUser() throws ServletException, IOException, DatabaseException {
		session.setAttribute("user", testUser);
		mockThatServlet.putParameter("username", testUser.getUsername());
		mockThatServlet.putParameter("cpr", testUser.getCpr());
		mockThatServlet.putParameter("name", testUser.getName());
		mockThatServlet.putParameter("institute", testUser.getInstitute());
		mockThatServlet.putParameter("consultant", testUser.getConsultant());
		
		mockThatServlet.setUrlPattern("/admin/editUser");
		
		new UserController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "userinfo");
		
		assertNotNull(session.getAttribute("confirmation"));
		assertEquals("Successfully updated user: " + testUser.getName(), session.getAttribute("confirmation"));
	}
}
