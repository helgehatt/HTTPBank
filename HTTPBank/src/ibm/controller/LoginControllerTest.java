package ibm.controller;

import static org.junit.Assert.*;
import ibm.test.MockThatServlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

public class LoginControllerTest {
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

		System.out.println(mockThatServlet.printInfo());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoginUserSucces() throws ServletException, IOException {
		mockThatServlet.putParameter("username", "Helge");
		mockThatServlet.putParameter("password", "hatt");
		
		new LoginController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), "user/accounts");
		
		assertNotNull(session.getAttribute("user"));
		assertNotNull(session.getAttribute("admin"));
		assertFalse((boolean)session.getAttribute("admin"));
		assertNotNull(session.getAttribute("currencies"));
		assertFalse(((ArrayList<String>)session.getAttribute("currencies")).isEmpty());

		System.out.println(mockThatServlet.printInfo());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testLoginUserFail() throws ServletException, IOException {
		mockThatServlet.putParameter("username", "Helge");
		mockThatServlet.putParameter("password", "ht");
		
		new LoginController().doPost(request, response);
		
		assertNotNull(mockThatServlet.getRedirectedTo());
		assertEquals(mockThatServlet.getRedirectedTo(), " ?s=0");

		assertNotNull(session.getAttribute("currencies"));
		assertFalse(((ArrayList<String>)session.getAttribute("currencies")).isEmpty());
		
		System.out.println(mockThatServlet.printInfo());
	}
}
