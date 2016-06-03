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
import ibm.resource.InputException;
import ibm.resource.User;

@WebServlet( "/admin/editUser" )
public class UserEditor extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HashMap<String, String> errors = new HashMap<String, String>();
    	
        String cpr = request.getParameter("cpr");
        String name = request.getParameter("name");
        String institute = request.getParameter("institute");
        String consultant = request.getParameter("consultant");
        
        try {
        	AttributeChecks.checkCpr(cpr);
        } catch (InputException e) {
        	errors.put("cpr", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkName(name);
        } catch (InputException e) {
        	errors.put("name", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkInstitute(institute);
        } catch (InputException e) {
        	errors.put("institute", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkConsultant(consultant);
        } catch (InputException e) {
        	errors.put("consultant", e.getMessage());
        }

        HttpSession session = request.getSession();
        
        if (errors.isEmpty()) {
        	int id = ((User) session.getAttribute("user")).getId();

        	// TODO: updateUser without username param.
        	String username = "user";
        	String password = "password";
        	DB.updateUser(id, username, password, cpr, name, institute, consultant);
			
			session.setAttribute("user", DB.getUser(id));
			response.sendRedirect("userinfo");
        } else {
        	session.setAttribute("errors", errors);
        	response.sendRedirect("edituser");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
