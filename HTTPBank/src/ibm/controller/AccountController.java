package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.test.TestData;

@WebServlet(urlPatterns = { "/user/getAccount", "/admin/getAccount" })
public class AccountController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String iban = request.getParameter("iban");
        
		HttpSession session = request.getSession();
		session.setAttribute("account", TestData.getAccountByIban(iban));
		
		response.sendRedirect("accountinfo");
    }
}
