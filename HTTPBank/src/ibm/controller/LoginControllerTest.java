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

public class LoginControllerTest {
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

	@SuppressWarnings("unchecked")
	@Test
	public void testLoginAdmin() throws ServletException, IOException {
		mockThatServlet.putParameter("username", "admin");
		mockThatServlet.putParameter("password", "");
		
		new LoginController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "admin/users");
		
		assertNotNull(session.getAttribute("users"));
		assertNotNull(session.getAttribute("admin"));
		assertTrue((boolean)session.getAttribute("admin"));
		assertNotNull(session.getAttribute("currencies"));
		assertFalse(((ArrayList<String>)session.getAttribute("currencies")).isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoginUserSucces() throws ServletException, IOException {
		mockThatServlet.putParameter("username", testUser.getUsername());
		mockThatServlet.putParameter("password", "password");
		
		new LoginController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "user/accounts");
		
		assertNotNull(session.getAttribute("user"));
		assertEquals(testUser.getConsultant() ,((User)session.getAttribute("user")).getConsultant());
		assertEquals(testUser.getCpr() ,((User)session.getAttribute("user")).getCpr());
		assertEquals(testUser.getId() ,((User)session.getAttribute("user")).getId());
		assertEquals(testUser.getInstitute() ,((User)session.getAttribute("user")).getInstitute());
		assertEquals(testUser.getName() ,((User)session.getAttribute("user")).getName());
		assertEquals(testUser.getUsername() ,((User)session.getAttribute("user")).getUsername());
		assertNotNull(session.getAttribute("admin"));
		assertFalse((boolean)session.getAttribute("admin"));
		assertNotNull(session.getAttribute("currencies"));
		assertFalse(((ArrayList<String>)session.getAttribute("currencies")).isEmpty());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoginUserFail() throws ServletException, IOException {
		mockThatServlet.putParameter("username", testUser.getUsername());
		mockThatServlet.putParameter("password", "NotCorrectPassword");
		
		new LoginController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), " ?s=0");

		assertNull(session.getAttribute("user"));
		
		assertNotNull(session.getAttribute("currencies"));
		assertFalse(((ArrayList<String>)session.getAttribute("currencies")).isEmpty());
	}
}
