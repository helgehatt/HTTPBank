package ibm.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.resource.InputException;
import ibm.resource.User;
import ibm.test.TestData;

@WebServlet("/admin/newUser")
public class UserCreator extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HashMap<String, String> errors = new HashMap<String, String>();
    	
        String cpr = request.getParameter("cpr");
        String name = request.getParameter("name");
        String institute = request.getParameter("institute");
        String consultant = request.getParameter("consultant");
        
        try {
        	checkCpr(cpr);
        } catch (InputException e) {
        	errors.put("cpr", e.getMessage());
        }
        
        try {
        	checkName(name);
        } catch (InputException e) {
        	errors.put("name", e.getMessage());
        }
        
        try {
        	checkConsultant(consultant);
        } catch (InputException e) {
        	errors.put("institute", e.getMessage());
        }
        
        try {
        	checkInstitute(institute);
        } catch (InputException e) {
        	errors.put("consultant", e.getMessage());
        }

        if (errors.isEmpty()) {
            TestData.getUsers().add(new User(cpr, name, institute, consultant));
    		response.sendRedirect("users");
        } else {
        	request.getSession().setAttribute("errors", errors);
        	response.sendRedirect("newuser");
        }
    }

    // TODO: Implement as thread safe static methods?
    private void checkCpr(String cpr) throws InputException {
    	if (cpr.length() < 5)
    		throw new InputException();
    }
    
    private void checkName(String name) throws InputException {
    	if (name.length() < 5) 
    		throw new InputException();    	
    }
    
    private void checkInstitute(String institute) throws InputException {
    	if (institute.length() < 5)
    		throw new InputException();
    }
    
    private void checkConsultant(String consultant) throws InputException {
    	if (consultant.length() < 5)
    		throw new InputException();
    }
}
