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
import ibm.resource.ExceptionHandler;

@WebServlet("/admin/closeAccount")
public class AccountCloser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
    	boolean transfer = request.getParameter("type").equals("transfer");    	
    	
    	Account account = (Account) request.getSession().getAttribute("account");
    	
    	String amountString = request.getParameter("amount");
    	double receiverAmount = 0;
    	
    	try {
    		receiverAmount = Double.parseDouble(amountString);
    	} catch (NumberFormatException e) {
    		ExceptionHandler.failure("Failed to parse the amount.", session, response, "cloesaccount");
    		return;
    	}
    	
    	if (transfer) {
    		int receiverId = 0;
    		try {
    			receiverId = Integer.parseInt(request.getParameter("to"));
    		} catch (NumberFormatException e) {
    			ExceptionHandler.failure("Failed to parse the ID.", session, response, "accounts");
    			return;
    		}
    		
    		String receiverCurrency = request.getParameter("currency");
    		
    		try {
    			String receiverDescription = account.getBalance() + " " + account.getCurrency() + " transfered from " + account.getNumber() + " before closing the account.";
				DB.deleteAccountWithTransfer(account.getId(), receiverId, receiverDescription, receiverAmount, receiverCurrency);
				ExceptionHandler.success("Transfer completed and successfully closed account: " + account.getNumber(), session);
			} catch (DatabaseException e) {
				ExceptionHandler.failure(e, "Failed to complete the transfer.", session, response, "cloesaccount");
	    		return;
			}
    	} else {
    		try {
    			DB.deleteAccount(account.getId());
    			ExceptionHandler.success("Successfully closed account: " + account.getNumber(), session);
    		} catch (DatabaseException e) {
    			ExceptionHandler.failure(e, "Failed to close the account.", session, response, "cloesaccount");
    			return;
    		}    		
    	}
    	response.sendRedirect("accounts");		
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath() + "/logout");
    }
}
