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

@WebServlet( "/admin/editAccount" )
public class AccountEditor extends HttpServlet {
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
        	AttributeChecks.checkCurrency(currency);
        } catch (InputException e) {
        	errors.put("currency", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkInterest(interest);
        } catch (InputException e) {
        	errors.put("interest", e.getMessage());
        }
        
        try {
        	AttributeChecks.checkBalance(balance);
        } catch (InputException e) {
        	errors.put("balance", e.getMessage());
        }

        HttpSession session = request.getSession();
        
        if (errors.isEmpty()) {
        	// TODO: Not thread safe!
	        Account account = (Account) session.getAttribute("account");
	        account.setType(type);
	        account.setNumber(number);
	        account.setIban(iban);
	        account.setCurrency(currency);
	        account.setInterest(Double.parseDouble(interest));
	        account.setBalance(Double.parseDouble(balance));
			response.sendRedirect("accountinfo");
        } else {
        	session.setAttribute("errors", errors);
        	response.sendRedirect("editaccount");
        }
    }
}
