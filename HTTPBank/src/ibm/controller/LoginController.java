package ibm.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkLogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username.equals("user") && password.equals("")) {
            UserTest test = new UserTest();
			HttpSession session = request.getSession();
			session.setAttribute("user", test.getUser());
			
        	RequestDispatcher dispatcer = request.getRequestDispatcher("/WEB-INF/jsp/user.jsp");
        	dispatcer.forward(request, response);
        } else if (username.equals("admin") && password.equals("")) {
        	AdminTest test = new AdminTest();
			HttpSession session = request.getSession();
			session.setAttribute("users", test.getUsers());
			
        	RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/admin.jsp");
			dispatcher.forward(request, response);
        } else {
        	RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
			dispatcher.forward(request, response);
        }
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
