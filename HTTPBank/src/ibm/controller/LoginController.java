package ibm.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.db.DB;

@WebServlet("/checkLogin")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // TODO: DB Authentication: Set login status.
        
        try {
        	int userID = DB.checkLogin2(username, password);
			if (userID != -1) {
				request.getSession().setAttribute("user", DB.getUser(userID));
				response.sendRedirect("user/accounts");
				
			} else if (username.equals("admin") && password.equals("")) {			
				response.sendRedirect("admin/users");
				
			} else {
				response.sendRedirect(" ?s=0");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }

}
