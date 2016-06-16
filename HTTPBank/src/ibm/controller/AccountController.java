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
import ibm.resource.Account;
import ibm.resource.AttributeChecks;
import ibm.resource.DatabaseException;
import ibm.resource.ExceptionHandler;
import ibm.resource.InputException;
import ibm.resource.User;

@WebServlet(urlPatterns = { "/admin/newAccount", "/admin/editAccount" })
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
		
    	HashMap<String, String> errors = new HashMap<String, String>();    	
        String name = request.getParameter("name");
    	String type = request.getParameter("type");
        String number = request.getParameter("number");
        String iban = request.getParameter("iban");
        String currency = request.getParameter("currency");
        String interestString = request.getParameter("interest");
        
        double interest = 0;
        
        try {
        	AttributeChecks.checkAccountName(name);
        } catch (InputException e) {
        	errors.put("name", e.getMessage());
        }
               
        try {
        	AttributeChecks.checkType(type);
        } catch (InputException e) {
        	errors.put("type", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkNumber(number);
        } catch (InputException e) {
        	errors.put("number", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkIban(iban);
        } catch (InputException e) {
        	errors.put("iban", e.getMessage());
        }
        
        try {
        	interest = AttributeChecks.checkInterest(interestString);
        } catch (InputException e) {
        	errors.put("interest", e.getMessage());
        }

		String path = request.getRequestURI().replace(request.getContextPath(), "");
		
		switch(path) {
		case "/admin/newAccount":
	        
	        if (errors.isEmpty()) {
				try {
					DB.createAccount(user.getId(), name, type, number, iban, currency, interest);
					ExceptionHandler.success("Successfully created new account: " + number, session);
					response.sendRedirect("accounts");
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to create the account.", session, response, "newaccount");
				}				
	        } else {
	        	session.setAttribute("errors", errors);
	        	response.sendRedirect("newaccount");
	        }
			
			break;
		case "/admin/editAccount":

	        if (errors.isEmpty()) {
		        int id = ((Account) session.getAttribute("account")).getId();		        			
				try {
					Account account = DB.updateAccount(id, name, type, number, iban, interest);
					ExceptionHandler.success("Successfully updated account: " + number, session);
					session.setAttribute("account", account);
					response.sendRedirect("accountinfo");
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to update the account.", session, response, "editaccount");
		    		return;
				}
	        } else {
	        	session.setAttribute("errors", errors);
	        	response.sendRedirect("editaccount");
	        }
	        
			break;
		}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath() + "/logout");
    }
}
