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

@WebServlet(urlPatterns = { "/admin/newUser", "/admin/editUser", "/admin/deleteUser" })
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		String path = request.getRequestURI().replace(request.getContextPath(), "");
		
		if (path.equals("/admin/deleteUser")) {
			try {
				String username = user.getUsername();
				DB.deleteUser(username);
				DatabaseException.success("Successfully deleted user: " + username, session);
				session.setAttribute("users", null);
            	response.sendRedirect("users");
			} catch (DatabaseException e) {
	    		DatabaseException.handleException(e, session, response, "deleteuser");
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
					DB.createUser(username, cpr, name, institute, consultant);
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
            	int id = user.getId();            	
            	try {
            		DB.updateUser(id, username, cpr, name, institute, consultant);
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
