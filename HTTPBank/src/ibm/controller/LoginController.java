package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.test.TestData;

@WebServlet("/checkLogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // TODO: DB Authentication: Set login status.
        
        if (username.equals("user") && password.equals("")) {
			HttpSession session = request.getSession();
			session.setAttribute("user", TestData.getUser());
			response.sendRedirect("user/accounts");
			
        } else if (username.equals("admin") && password.equals("")) {
			HttpSession session = request.getSession();
			session.setAttribute("user", null);
			session.setAttribute("users", TestData.getUsers());
			response.sendRedirect("admin/users");
			
        } else {
        	response.sendRedirect("/login");
        }
	}

}
