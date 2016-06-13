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
		String id = request.getParameter("id");
	    String from = request.getParameter("from");
		String amountString = request.getParameter("change");
		
		int fromId = 0;
		double amount = 0;
		
		try {
			fromId = Integer.parseInt(id);
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
					DB.createTransaction(fromId, "Deposited " + amount + " to " + from , amount);
					DatabaseException.success("Deposit completed successfully.", session);
				} catch (DatabaseException e) {
		    		DatabaseException.handleException(e, session);
				}
				break;
			case "withdrawal":
				try {
					DB.createTransaction(fromId, "Withdrew " + amount + " from " + from , -amount);
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
