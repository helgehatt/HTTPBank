package ibm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.db.DB;
import ibm.resource.Account;
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
        String currency = request.getParameter("currency");
		String amountString = request.getParameter("amount");
		
		int fromId = 0;
		double amount = 0;
		
		try {
			fromId = Integer.parseInt(from);
		} catch (NumberFormatException e) {
			// TODO: Throw new "Something went wrong, try refreshing the page" exception.
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
				AttributeChecks.checkCurrency(currency);
			} catch (InputException e) {
	        	errors.put("currency", e.getMessage());
			}
			
		} else {
			try {
				AttributeChecks.checkNumber(to);
			} catch (InputException e) {
	        	errors.put("to", e.getMessage());
			}
		}
		
		try {
			amount = AttributeChecks.checkAmount(amountString);
		} catch (InputException e) {
        	errors.put("amount", e.getMessage());
		}
		
		if (errors.isEmpty()) {
			try {
				Account toAcc = (Account) DB.getAccountByNumber(to);
				if (toAcc != null)
					DB.createTransaction(fromId, toAcc.getId(), "Transfer to " + to, "Transfer from " + from, amount);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
        	request.getSession().setAttribute("errors", errors);
		}
		
		if (international)
			response.sendRedirect("international");
		else
			response.sendRedirect("transfer");
        
        

        // TODO: Message objects?
        // String message = request.getParameter("message"); // Optional
    	// if (message!=null) toAccount.getUser().getMessages().add(new Message(message));
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
