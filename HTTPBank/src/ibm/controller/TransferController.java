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
        String formIdString = request.getParameter("from-id");
        String fromNumber = request.getParameter("from-number");
        String toNumber = request.getParameter("to-number");
        String inputAmount = request.getParameter("input-amount");
		String withdrawnCurrency = request.getParameter("withdrawn-currency");
		String message = request.getParameter("message");
		
		int fromId = 0;
		double input = 0;
		
		try {
			fromId = Integer.parseInt(formIdString);
		} catch (NumberFormatException e) {
			ExceptionHandler.failure("Failed parsing the ID.", session, response, "accounts");
			return;
		}
		
		try {
			input = AttributeChecks.checkAmount(inputAmount);
		} catch (InputException e) {
        	errors.put("inputAmount", e.getMessage());
		}		
		
		if (international) {
			String withdrawnAmount = request.getParameter("withdrawn-amount");
	        String inputCurrency = request.getParameter("input-currency");
	        String bic = request.getParameter("to-bic");
	        
	        double withdrawn = 0;
	        
	        try {
	        	withdrawn = AttributeChecks.checkAmount(withdrawnAmount);
	        } catch (InputException e) {
	        	errors.put("withdrawnAmount", e.getMessage());
	        }
	        
			try {
				AttributeChecks.checkIban(toNumber);
			} catch (InputException e) {
	        	errors.put("toNumber", e.getMessage());
			}
			
			try {
				AttributeChecks.checkBic(bic);
			} catch (InputException e) {
	        	errors.put("toBic", e.getMessage());
			}
			
			if (errors.isEmpty()) {
				try {
					String senderDescription = inputAmount + " " + inputCurrency + " transfered to " + toNumber;
					String receiverDescription = inputAmount + " " + inputCurrency + " transfered from " + fromNumber;
					
					DB.createTransaction(TransBy.IBAN, fromId, toNumber, senderDescription, receiverDescription, -withdrawn, withdrawnCurrency);
					ExceptionHandler.success("Transfer to " + toNumber + " completed successfully.", session);
					if (!message.isEmpty()) {
						try {
					        DB.createMessage(message, fromId, toNumber, TransBy.IBAN);
					        ExceptionHandler.success("Transfer to " + toNumber + " completed successfully and message sent.", session);							
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
				AttributeChecks.checkNumber(toNumber);
			} catch (InputException e) {
	        	errors.put("toNumber", e.getMessage());
			}
			
			if (errors.isEmpty())
				try {
					String senderDescription = inputAmount + " " + withdrawnCurrency + " transfered to " + toNumber;
					String receiverDescription = inputAmount + " " + withdrawnCurrency + " transfered from " + fromNumber;
					
					DB.createTransaction(TransBy.NUMBER, fromId, toNumber, senderDescription, receiverDescription, -input, withdrawnCurrency);
					ExceptionHandler.success("Transfer to " + toNumber + " completed successfully.", session);
					if (!message.isEmpty()) {
						try {
					        DB.createMessage(message, fromId, toNumber, TransBy.NUMBER);
					        ExceptionHandler.success("Transfer to " + toNumber + " completed successfully and message sent.", session);							
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
