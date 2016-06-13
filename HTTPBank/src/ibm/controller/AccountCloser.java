package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;
import ibm.resource.Account;
import ibm.resource.DatabaseException;

@WebServlet("/admin/closeAccount")
public class AccountCloser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
    	boolean transfer = request.getParameter("type").equals("transfer");    	
    	
    	Account account = (Account) request.getSession().getAttribute("account");
    	int accountId = account.getId();
    	
    	String amountString = request.getParameter("amount");
    	double amount = 0;
    	
    	try {
    		amount = Double.parseDouble(amountString);
    	} catch (NumberFormatException e) {
    		DatabaseException.failure("Failed to parse the amount.", session, response, "cloesaccount");
    		return;
    	}
    	
    	if (transfer) {
    		int receiverId = 0;
    		try {
    			receiverId = Integer.parseInt(request.getParameter("to"));
    		} catch (NumberFormatException e) {
	    		DatabaseException.failure("Failed to parse the ID.", session, response, "accounts");
    			return;
    		}
    		
    		try {
				DB.createTransaction(receiverId, "Closed account: " + account.getNumber(), amount);
				DatabaseException.success("Transfer completed, but account did not close successfully.", session);
			} catch (DatabaseException e) {
	    		DatabaseException.failure("Failed to complete the transfer.", session, response, "cloesaccount");
	    		return;
			}
    	}
    	
		try {
			DB.deleteAccount(accountId);
			DatabaseException.success("Successfully closed account: " + account.getNumber(), session);
	    	response.sendRedirect("accounts");
		} catch (DatabaseException e) {
    		DatabaseException.failure("Failed to close the account.", session, response, "cloesaccount");
		}
    	
    	
    	
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
