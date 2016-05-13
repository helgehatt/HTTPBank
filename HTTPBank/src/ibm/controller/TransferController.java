package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.resource.Account;
import ibm.resource.Transaction;
import ibm.resource.User;
import ibm.test.TestData;

@WebServlet("/admin/doTransfer")
public class TransferController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		String transfer = request.getParameter("transfer");
		
		switch(transfer) {
		case "user-dom":
	        newTransfer(request, response, false);
	        response.sendRedirect("transfer");
			break;
		case "user-int":
	        newTransfer(request, response, true);
	        response.sendRedirect("transfer");
			break;
		case "admin-dom":
	        newTransfer(request, response, false);
	        response.sendRedirect("transfer");
			break;
		case "admin-int":
	        newTransfer(request, response, true);
	        response.sendRedirect("international");
			break;
		}
	}

	private void newTransfer(HttpServletRequest request, HttpServletResponse response, boolean international) {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        double amount = Double.parseDouble(request.getParameter("amount"));

		// TODO: "from" should be a dropdown
        Account fromAccount = getDomesticAccount(from);
        Account toAccount;
        
        if (international) {
            String bic = request.getParameter("bic");
        	toAccount = getInternationalAccount(to, bic);
            String currency = request.getParameter("currency");
        	amount = amount * getConversionRate(currency);
        } else {
            toAccount = getDomesticAccount(to);
        }

        fromAccount.getTransactions().add(new Transaction("Transfer to " + to, -amount));
        toAccount.getTransactions().add(new Transaction("Transfer from " + from, amount));

        // TODO: Message objects?
        // String message = request.getParameter("message"); // Optional
    	// if (message!=null) toAccount.getUser().getMessages().add(new Message(message));
	}
	
	private Account getDomesticAccount(String number) {
		for (User user : TestData.getUsers())
			for (Account account : user.getAccounts())
				if (number.equals(account.getNumber()))
					return account;
		return null;
	}
	
	private Account getInternationalAccount(String iban, String bic) {
		// TODO: How to handle foreign accounts?
		// Imply using bic to forward the task to another bank?
		// Our DB does not contain international accounts.
		for (User user : TestData.getUsers())
			for (Account account : user.getAccounts())
				if (iban.equals(account.getIban()))
					return account;
		return null;
	}
	
	private double getConversionRate(String currency) {
		// TODO: Implement currency converter
		return 1;
	}

}
