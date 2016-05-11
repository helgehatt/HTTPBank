package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getTransfer")
public class TransferController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String bic = request.getParameter("bic");
        String amount = request.getParameter("amount");
        String currency = request.getParameter("currency");
        String message = request.getParameter("message"); // Optional
        
        if (bic == null) {
        	System.out.println("Domestic");
        	// To <- account.number
        	// Currency <- from.account.currency
        } else {
        	System.out.println("International");
        	// To <- account.iban
        }
        
        System.out.println(from);
        System.out.println(to);
        System.out.println(bic);
        System.out.println(amount);
        System.out.println(currency);
        System.out.println(message);
	}

}
