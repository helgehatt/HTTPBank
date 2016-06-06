package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ibm.db.DB;

@WebServlet("/admin/getUser")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = 0;
        
        try {
        	id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
			response.sendError(418, "Lost connection to the database.");
			return;
		}
        
		request.getSession().setAttribute("user", DB.getUser(id));
		
		response.sendRedirect("accounts");
	
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
