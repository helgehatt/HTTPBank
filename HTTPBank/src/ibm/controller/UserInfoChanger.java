package ibm.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;
import ibm.db.DB.USER;
import ibm.resource.DatabaseException;
import ibm.resource.ExceptionHandler;
import ibm.resource.User;

@WebServlet("/user/doChangeInfo")
public class UserInfoChanger extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	User user = (User) session.getAttribute("user");    	
    	
    	HashMap<String, String> errors = new HashMap<String, String>();
    	String cPassword = request.getParameter("c-password");
    	String nUsername = request.getParameter("username");
    	String nPassword = request.getParameter("n-password");
    	String rPassword = request.getParameter("r-password");
    	    	
    	try {
			if (DB.checkLogin(user.getUsername(), cPassword) < 1) {
				errors.put("cpassword", "Incorrect password");
			}
		} catch (DatabaseException e) {
			ExceptionHandler.failure(e, "Failed to check login information.", session, response, "changeinfo");
    		return;
		}
    	
    	if (!nPassword.equals(rPassword)) {
    		errors.put("rpassword", "Passwords do not match");
    	}
    	
    	if (errors.isEmpty()) {    	
    		int id = user.getId();
    		
    		if (!nUsername.isEmpty()) {
    			try {
					User newUser = DB.updateUser(id, nUsername, USER.USERNAME);
					ExceptionHandler.success("Successfully changed user name.", session);
					session.setAttribute("user", newUser);
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to change user name.", session, response, "changeinfo");
		    		return;
				}
    		}
    		
    		if (!nPassword.isEmpty()) {
    			try {
					User newUser = DB.updateUser(id, nPassword, USER.PASSWORD);
					ExceptionHandler.success("Successfully changed user name and password.", session);
					session.setAttribute("user", newUser);
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to change password.", session, response, "changeinfo");
		    		return;
				}
    		}
        	response.sendRedirect("userinfo");
    	} else {
    		session.setAttribute("errors", errors);
    		response.sendRedirect("changeinfo");
    	}
    	
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
