package ibm.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.resource.Account;
import ibm.resource.InputException;
import ibm.resource.User;

@WebServlet("/admin/newAccount")
public class AccountCreator extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HashMap<String, String> errors = new HashMap<String, String>();
    	
        String type = request.getParameter("type");
        String number = request.getParameter("number");
        String iban = request.getParameter("iban");
        String currency = request.getParameter("currency");
        String interest = request.getParameter("interest");
        String balance = request.getParameter("balance");
        
        try {
        	checkType(type);
        } catch (InputException e) {
        	errors.put("type", e.getMessage());
        }
        
        try {
        	checkNumber(number);
        } catch (InputException e) {
        	errors.put("number", e.getMessage());
        }
        
        try {
        	checkIban(iban);
        } catch (InputException e) {
        	errors.put("iban", e.getMessage());
        }
        
        try {
        	checkCurrency(currency);
        } catch (InputException e) {
        	errors.put("currency", e.getMessage());
        }
        
        try {
        	checkInterest(interest);
        } catch (InputException e) {
        	errors.put("interest", e.getMessage());
        }
        
        try {
        	checkBalance(balance);
        } catch (InputException e) {
        	errors.put("balance", e.getMessage());
        }

        HttpSession session = request.getSession();
        
        if (errors.isEmpty()) {
	        User user = (User) session.getAttribute("user");
	    	user.getAccounts().add(new Account(type, number, iban, currency, Double.parseDouble(interest), Double.parseDouble(balance)));
			response.sendRedirect("accounts");
        } else {
        	session.setAttribute("errors", errors);
        	response.sendRedirect("newaccount");
        }
    }

    // TODO: Implement as thread safe static methods?
    // Not needed to make thread-safe, this is completely thread-safe as static or not. Only reason it would not be thread-safe is if threads had to share data, in this case the scope prevents threads from using the same variable even as static.
    private void checkType(String type) throws InputException {
    	if (type.length() < 5)
    		throw new InputException();
    }
    
    private void checkNumber(String number) throws InputException {
    	if (number.length() < 5)
    		throw new InputException();
    }
    
    private void checkIban(String iban) throws InputException {
    	if (iban.length() < 5)
    		throw new InputException();
    }
    
    private void checkCurrency(String currency) throws InputException {
    	if (currency.length() != 3)
    		throw new InputException();
    }
    
    private void checkInterest(String interest) throws InputException {
    	try {
    		Double.parseDouble(interest);
    	} catch (NumberFormatException e) {
    		throw new InputException();
    	}
    }
    
    private void checkBalance(String balance) throws InputException {
    	try {
    		Double.parseDouble(balance);
    	} catch (NumberFormatException e) {
    		throw new InputException();
    	}
    }
    
}
