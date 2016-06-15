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
import ibm.resource.ExceptionHandler;
import ibm.resource.InputException;

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
		String amountString = request.getParameter("change");
		String currency = request.getParameter("from-currency");
		String message = request.getParameter("message");
		
		int fromId = 0;
		double amount = 0;
		
		try {
			fromId = Integer.parseInt(id);
		} catch (NumberFormatException e) {
			ExceptionHandler.failure("Failed parsing the ID.", session, response, "accounts");
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
			
			if (errors.isEmpty()) {
				try {
					DB.createTransaction(TransBy.IBAN, fromId, to, "Transfer to " + to, "Transfer from " + from, -amount, currency);
					ExceptionHandler.success("Transfer to " + to + " completed successfully.", session);
					if (!message.isEmpty()) {
						try {
					        DB.createMessage(message, fromId, to, TransBy.IBAN);
					        ExceptionHandler.success("Transfer to " + to + " completed successfully and message sent.", session);							
						} catch (DatabaseException e) {
							ExceptionHandler.failure(e, "Failed to send the message.", session);							
						}
					}
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to complete the transfer.", session);
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
					DB.createTransaction(TransBy.NUMBER, fromId, to, "Transfer to " + to, "Transfer from " + from, -amount, currency);
					ExceptionHandler.success("Transfer to " + to + " completed successfully.", session);
					if (!message.isEmpty()) {
						try {
					        DB.createMessage(message, fromId, to, TransBy.NUMBER);
					        ExceptionHandler.success("Transfer to " + to + " completed successfully and message sent.", session);							
						} catch (DatabaseException e) {
							ExceptionHandler.failure(e, "Failed to send the message.", session);
						}
					}
				} catch (DatabaseException e) {
					ExceptionHandler.failure(e, "Failed to complete the transfer.", session);
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
