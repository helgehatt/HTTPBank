package ibm.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.db.DB;
import ibm.db.DB.TransBy;
import ibm.resource.CurrencyConverter;
import ibm.resource.InputException;

@WebServlet(urlPatterns = { "/user/doTransfer", "/admin/doTransfer" } )
public class TransferController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HashMap<String, String> errors = new HashMap<String, String>();
    	
		boolean international = request.getParameter("international") != null;
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String bic = request.getParameter("bic");
        String toCurrency = request.getParameter("currency");
		String amountString = request.getParameter("amount");
		
		int fromId = 0;
		double amount = 0;
		
		try {
			fromId = Integer.parseInt(from);
		} catch (NumberFormatException e) {
			response.sendError(418, "Lost connection to the database.");
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
				AttributeChecks.checkCurrency(toCurrency);
			} catch (InputException e) {
	        	errors.put("currency", e.getMessage());
			}
			
			if (errors.isEmpty()) {
				String fromCurrency = DB.getAccount(fromId).getCurrency();
				Double toAmount = CurrencyConverter.convert(fromCurrency, toCurrency, amount);
				if (null == toAmount) {
					response.sendError(419, "Could not complete the conversion.");
					return;
				}
				DB.createTransaction(TransBy.IBAN, fromId, to, "Transfer to " + to, "Transfer from " + from, -amount, toAmount);
			} else
	        	request.getSession().setAttribute("errors", errors);

			response.sendRedirect("international");
			
		} else {
			try {
				AttributeChecks.checkNumber(to);
			} catch (InputException e) {
	        	errors.put("to", e.getMessage());
			}
			
			if (errors.isEmpty())
				DB.createTransaction(TransBy.NUMBER, fromId, to, "Transfer to " + to, "Transfer from " + from, -amount, amount);
			else
	        	request.getSession().setAttribute("errors", errors);

			response.sendRedirect("transfer");
		}  
        
        // TODO: Message objects?
        // String message = request.getParameter("message"); // Optional
    	// if (message!=null) toAccount.getUser().getMessages().add(new Message(message));
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
