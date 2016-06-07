package ibm.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.db.DB;
import ibm.db.DB.TransBy;
import ibm.resource.Account;

@WebServlet("/admin/closeAccount")
public class AccountCloser extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	boolean transfer = request.getParameter("type").equals("transfer");
    	
    	Account account = (Account) request.getSession().getAttribute("account");
    	int accountId = account.getId();
    	
    	String amountString = request.getParameter("amount");
    	double amount = 0;
    	
    	try {
    		amount = Double.parseDouble(amountString);
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    	}
    	
    	if (transfer) {
    		int receiverId = 0;
    		try {
    			receiverId = Integer.parseInt(request.getParameter("to"));
    		} catch (NumberFormatException e) {
    			response.sendError(418, "Lost connection to the database.");
    			return;
    		}
    		
    		DB.createTransaction(TransBy.ID, accountId, "" + receiverId, "", "Closed account: " + account.getNumber(), 0, amount);
    		DB.deleteAccount(accountId);
    	} else {
    		// Assume the amount has been withdrawn.
    		DB.deleteAccount(accountId);
    	}
    	
    	response.sendRedirect("accounts");
    	
    	
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
