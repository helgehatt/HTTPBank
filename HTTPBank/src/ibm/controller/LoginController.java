package ibm.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;
import ibm.resource.DatabaseException;

@WebServlet("/checkLogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final ArrayList<String> CURRENCIES = initCurrencies();
	
	private static ArrayList<String> initCurrencies() {
		try {
			return DB.getCurrencies();
		} catch (DatabaseException e) {
			// TODO: Any suggestions?
			e.printStackTrace();
		}
		return null;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

		session.setAttribute("currencies", CURRENCIES);
        
        try {
        	int userID = DB.checkLogin(username, password);
			if (userID > 0) {
				session.setAttribute("admin", false);
				session.setAttribute("user", DB.getUser(userID));
				response.sendRedirect("user/accounts");
				
			} else if (username.equals("admin") && password.equals("")) {	
				session.setAttribute("admin", true);
				request.getSession().setAttribute("users", DB.getUsers());
				response.sendRedirect("admin/users");				
			} else {
				response.sendRedirect(" ?s=0");
			}
		} catch(DatabaseException e) {
    		DatabaseException.handleException(e, session, response, "login");			
		}
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }

}
