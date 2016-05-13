package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkLogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // TODO: DB Authentication: Set login status.
        
        if (username.equals("user") && password.equals("")) {
			response.sendRedirect("user/accounts");
			
        } else if (username.equals("admin") && password.equals("")) {
			HttpSession session = request.getSession();
			session.removeAttribute("user");
			session.removeAttribute("account");
			
			response.sendRedirect("admin/users");
			
        } else {
        	response.sendRedirect(" ");
        }
	}

}
