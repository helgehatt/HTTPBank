package ibm.controller;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.db.DB;
import ibm.resource.AttributeChecks;
import ibm.resource.InputException;

@WebServlet(urlPatterns = "/admin/doDepositWithdrawal"  )
public class WithdrawalController extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> errors = new HashMap<String, String>();
		String type = request.getParameter("type");
	    String ID = request.getParameter("from");
	    String toCurrency = request.getParameter("to-currency");
		String fromCurrency = request.getParameter("fromCurrency");
		String amountString = request.getParameter("change");
		
		int fromId = 0;
		double amount = 0;
		
		try {
			fromId = Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			response.sendError(418, "Lost connection to the database.");
			return;
		}
		
		try {
			amount = Double.parseDouble(amountString);
		} catch (NumberFormatException e) {
			errors.put("amount", "Please enter only digits.");
		}
		
		try { 
			AttributeChecks.checkCurrency(toCurrency);
		} catch (InputException e) {
			errors.put("to-currency", e.getMessage());
		}
		
		try { 
			AttributeChecks.checkCurrency(fromCurrency);
		} catch (InputException e) {
			errors.put("from-currency", e.getMessage());
		}
		
		if(type.equals("deposit")) {
			if(errors.isEmpty()) {
				DB.createTransaction(fromId, "Deposited " + amount + " to " + fromId , amount);
			} else {
				request.getSession().setAttribute("errors", errors);
			}
			response.sendRedirect("depositwithdrawal");
		} else if(type.equals("withdrawal")) {
			if(errors.isEmpty()) {
				DB.createTransaction(fromId, "Withdrawed " + amount + " from " + fromId , -amount);
			} else {
				request.getSession().setAttribute("errors", errors);
			}
			response.sendRedirect("depositwithdrawal");
		}
	
	}
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }

}
