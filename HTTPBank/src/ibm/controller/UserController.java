package ibm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;
import ibm.resource.AttributeChecks;
import ibm.resource.DatabaseException;
import ibm.resource.ExceptionHandler;
import ibm.resource.InputException;
import ibm.resource.User;

@WebServlet(urlPatterns = { "/admin/newUser", "/admin/editUser", "/admin/deleteUser", "/admin/resetPassword" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String path = request.getRequestURI().replace(request.getContextPath(), "");
		
		switch (path) {
		case "/admin/deleteUser":
			try {
				DB.deleteUser(user.getId());
				ExceptionHandler.success("Successfully deleted the user: " + user.getName(), session);
				
				@SuppressWarnings("unchecked")
				ArrayList<User> users = (ArrayList<User>) session.getAttribute("users");
				users.remove(user);
				session.setAttribute("users", users);
				
            	response.sendRedirect("users");
			} catch (DatabaseException e) {
				ExceptionHandler.failure(e, "Failed to delete the user.", session, response, "deleteuser");
			}
			return;
		case "/admin/resetPassword":
			try {
				DB.resetPassword(user.getId());
				ExceptionHandler.success("Successfully reset " + user.getName() + "'s password.", session);
            	response.sendRedirect("userinfo");
			} catch (DatabaseException e) {
				ExceptionHandler.failure(e, "Failed to reset the user's password.", session, response, "resetpassword");
			}
			return;
		}
		
		
    	HashMap<String, String> errors = new HashMap<String, String>();    	
    	String username = request.getParameter("username");
        String cpr = request.getParameter("cpr");
        String name = request.getParameter("name");
        String institute = request.getParameter("institute");
        String consultant = request.getParameter("consultant");

        
        try {
        	AttributeChecks.checkUserName(username);
        } catch (InputException e) {
        	errors.put("username", e.getMessage());
        }
        
        try {
        	cpr = AttributeChecks.checkCpr(cpr);
        } catch (InputException e) {
        	errors.put("cpr", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkRealName(name);
        } catch (InputException e) {
        	errors.put("name", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkConsultant(consultant);
        } catch (InputException e) {
        	errors.put("institute", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkInstitute(institute);
        } catch (InputException e) {
        	errors.put("consultant", e.getMessage());
        }
		
    	switch(path) {
    	case "/admin/newUser":
            if (errors.isEmpty()) {
    			try {
					User newUser = DB.createUser(username, cpr, name, institute, consultant);
					ExceptionHandler.success("Successfully created new user: " + name, session);
					
    				@SuppressWarnings("unchecked")
					ArrayList<User> users = (ArrayList<User>) session.getAttribute("users");
					users.add(newUser);
					Collections.sort(users);
					session.setAttribute("users", users);
					
	            	response.sendRedirect("users");
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to create the user.", session, response, "newuser");
				}    			
            } else {
            	request.getSession().setAttribute("errors", errors);            	
            	response.sendRedirect("newuser");
            }
            
    		break;
    	case "/admin/editUser":            
            if (errors.isEmpty()) {           	
            	try {
            		User newUser = DB.updateUser(user.getId(), username, cpr, name, institute, consultant);
            		ExceptionHandler.success("Successfully updated user: " + name, session);
					session.setAttribute("user", newUser);
	    			response.sendRedirect("userinfo");
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to update the user.", session, response, "edituser");
				}
            } else {
            	session.setAttribute("errors", errors);
            	response.sendRedirect("edituser");
            }
    		
    		break;
    	}	
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath() + "/logout");
    }
}
