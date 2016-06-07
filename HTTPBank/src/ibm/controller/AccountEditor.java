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
import ibm.resource.InputException;

@WebServlet( "/admin/editAccount" )
public class AccountEditor extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HashMap<String, String> errors = new HashMap<String, String>();
    	
        String accountName = request.getParameter("name");
    	String type = request.getParameter("type");
        String number = request.getParameter("number");
        String iban = request.getParameter("iban");
        String currency = request.getParameter("currency");
        String interestString = request.getParameter("interest");
        String balanceString = request.getParameter("balance");
        
        double interest = 0;
        double balance = 0;
        
        try {
        	AttributeChecks.checkAccountName(accountName);
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
        	AttributeChecks.checkCurrency(currency);
        } catch (InputException e) {
        	errors.put("currency", e.getMessage());
        }
        
        try {
        	interest = AttributeChecks.checkInterest(interestString);
        } catch (InputException e) {
        	errors.put("interest", e.getMessage());
        }
        
        try {
        	balance = AttributeChecks.checkBalance(balanceString);
        } catch (InputException e) {
        	errors.put("balance", e.getMessage());
        }

        HttpSession session = request.getSession();
        
        if (errors.isEmpty()) {
	        int id = ((Account) session.getAttribute("account")).getId();
	        			
			DB.updateAccount(id, accountName, type, number, iban, currency, interest, balance);
			
			session.setAttribute("account", DB.getAccountByNumber(number));
			response.sendRedirect("accountinfo");
        } else {
        	session.setAttribute("errors", errors);
        	response.sendRedirect("editaccount");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
