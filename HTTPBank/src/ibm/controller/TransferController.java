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
import ibm.db.DB.TransBy;
import ibm.resource.AttributeChecks;
import ibm.resource.DatabaseException;
import ibm.resource.InputException;
import ibm.resource.CurrencyConverter;

@WebServlet(urlPatterns = { "/user/doTransfer", "/admin/doTransfer" } )
public class TransferController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
    	HashMap<String, String> errors = new HashMap<String, String>();    	
		boolean international = request.getParameter("international") != null;
        String id = request.getParameter("id");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String bic = request.getParameter("bic");
		String amountString = request.getParameter("amount");
		String withdrawnString = request.getParameter("change");
		String message = request.getParameter("message");
		String toCurrency = request.getParameter("to-currency");
		Double receivedAmount;
		String receiverCurrency;
		
		int fromId = 0;
		double amount = 0;
		double withdrawn = 0;
		
		try {
			fromId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
    		DatabaseException.failure("Failed parsing the ID.", session, response, "accounts");
			return;
		}
		
		try {
			amount = AttributeChecks.checkAmount(amountString);
		} catch (InputException e) {
        	errors.put("amount", e.getMessage());
		}
		
		
		if (international) {
			try {
				AttributeChecks.checkIban(to);
			} catch (InputException e) {
	        	errors.put("to", e.getMessage());
			}
			
			try {
				AttributeChecks.checkBic(bic);
			} catch (InputException e) {
	        	errors.put("bic", e.getMessage());
			}
			
			try {
				withdrawn = Double.parseDouble(withdrawnString);
			} catch (NumberFormatException e) {
				errors.put("amount", "Please enter only digits.");
			}
			
			if (errors.isEmpty()) {
				try {

					receiverCurrency = DB.getReceiverCurrency(to, TransBy.IBAN);
					receivedAmount = CurrencyConverter.convert(toCurrency, receiverCurrency, amount);
					DB.createTransaction(TransBy.IBAN, fromId, to, "Transfer to " + to, "Transfer from " + from, -withdrawn, receivedAmount);
					DatabaseException.success("Transfer to " + to + " completed successfully.", session);
					if (!message.isEmpty()) {
						try {
					        DB.createMessage(message, fromId, to, TransBy.IBAN);
							DatabaseException.success("Transfer to " + to + " completed successfully and message sent.", session);							
						} catch (DatabaseException e) {
				    		DatabaseException.failure("Failed to send the message.", session);							
						}
					}
				} catch (DatabaseException e) {
		    		DatabaseException.failure("Failed to complete the transfer.", session);
				}
			} else
	        	session.setAttribute("errors", errors);

			response.sendRedirect("international");
			
		} else {
			try {
				AttributeChecks.checkNumber(to);
			} catch (InputException e) {
	        	errors.put("to", e.getMessage());
			}
			
			if (errors.isEmpty())
				try {
					receiverCurrency = DB.getReceiverCurrency(to, TransBy.NUMBER);
					receivedAmount = CurrencyConverter.convert(toCurrency, receiverCurrency, amount);
					DB.createTransaction(TransBy.NUMBER, fromId, to, "Transfer to " + to, "Transfer from " + from, -amount, amount);
					DatabaseException.success("Transfer to " + to + " completed successfully.", session);
					if (!message.isEmpty()) {
						try {
					        DB.createMessage(message, fromId, to, TransBy.NUMBER);
							DatabaseException.success("Transfer to " + to + " completed successfully and message sent.", session);							
						} catch (DatabaseException e) {
				    		DatabaseException.failure("Failed to send the message.", session);
						}
					}
				} catch (DatabaseException e) {
		    		DatabaseException.failure("Failed to complete the transfer.", session);
				}
			else
	        	session.setAttribute("errors", errors);

			response.sendRedirect("transfer");
		}  
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
