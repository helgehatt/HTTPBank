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

@WebServlet(urlPatterns = "/admin/doDepositWithdrawal"  )
public class WithdrawalController extends HttpServlet{
	private static final long serialVersionUID = 1L;
		
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();
    	
		HashMap<String, String> errors = new HashMap<String, String>();		
		String type = request.getParameter("type");
	    String ID = request.getParameter("from");
		String amountString = request.getParameter("change");
		
		int fromId = 0;
		double amount = 0;
		
		try {
			fromId = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
    		DatabaseException.handleException(new DatabaseException(), session, response, "depositwithdrawal");
			return;
		}
		
		try {
			amount = AttributeChecks.checkAmount(amountString);
		} catch (InputException e) {
			errors.put("amount", e.getMessage());
		}
		
		if (errors.isEmpty()) {
			switch(type) {
			case "deposit":
				try {
					DB.createTransaction(fromId, "Deposited " + amount + " to " + fromId , amount);
					DatabaseException.success("Deposit completed successfully.", session);
				} catch (DatabaseException e) {
		    		DatabaseException.handleException(e, session);
				}
				break;
			case "withdrawal":
				try {
					DB.createTransaction(fromId, "Withdrew " + amount + " from " + fromId , -amount);
					DatabaseException.success("Withdrawal completed successfully.", session);
				} catch (DatabaseException e) {
		    		DatabaseException.handleException(e, session);
				}
				break;
			}
		} else 
			request.getSession().setAttribute("errors", errors);
		
		response.sendRedirect("depositwithdrawal");		
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }

}
