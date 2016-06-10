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
import ibm.resource.AttributeChecks;
import ibm.resource.DatabaseException;
import ibm.resource.InputException;
import ibm.resource.User;

@WebServlet(urlPatterns = { "/admin/newUser", "/admin/editUser" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
    	HashMap<String, String> errors = new HashMap<String, String>();    	
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
        String cpr = request.getParameter("cpr");
        String name = request.getParameter("name");
        String institute = request.getParameter("institute");
        String consultant = request.getParameter("consultant");

		String path = request.getRequestURI().replace(request.getContextPath(), "");
        
        try {
        	AttributeChecks.checkUserName(username);
        } catch (InputException e) {
        	errors.put("username", e.getMessage());
        }
        
        if (path.equals("/admin/newUser") || path.equals("/admin/editUser") && !password.isEmpty()) {
	        try {
	        	AttributeChecks.checkPassword(password);
	        } catch (InputException e) {
	        	errors.put("password", e.getMessage());
	        }
        }
        
        try {
        	AttributeChecks.checkCpr(cpr);
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
					DB.createUser(username, password, cpr, name, institute, consultant);
					DatabaseException.success("Successfully created new user: " + username, session);
					session.setAttribute("users", DB.getUsers());
	            	response.sendRedirect("users");
				} catch (DatabaseException e) {
		    		DatabaseException.handleException(e, session, response, "newuser");
				}    			
            } else {
            	request.getSession().setAttribute("errors", errors);            	
            	response.sendRedirect("newuser");
            }
            
    		break;
    	case "/admin/editUser":            
            if (errors.isEmpty()) {
            	int id = ((User) session.getAttribute("user")).getId();            	
            	try {
            		if (password.isEmpty())
                		// TODO: Remove password parameter
            			DB.updateUser(id, username, password, cpr, name, institute, consultant);
            		else
            			DB.updateUser(id, username, password, cpr, name, institute, consultant);
					DatabaseException.success("Successfully updated user: " + username, session);
					session.setAttribute("user", DB.getUser(id));
	    			response.sendRedirect("userinfo");
				} catch (DatabaseException e) {
		    		DatabaseException.handleException(e, session, response, "edituser");
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
    	response.sendRedirect(request.getContextPath());
    }
}
