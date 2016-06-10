package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;
import ibm.resource.DatabaseException;

@WebServlet(urlPatterns = { "/user/getAccount", "/admin/getAccount", "/admin/getUser", "/admin/findUsers" })
public class ObjectGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();        
        
		String path = request.getRequestURI().replace(request.getContextPath(), "");
		
		if (path.equals("/admin/findUsers")) {
			String input = request.getParameter("search");
			// TODO: call DB
			return;
		}
		
        int id = 0;
        
        try {
        	id = Integer.parseInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
    		DatabaseException.handleException(new DatabaseException(), session, response, "accounts");
			return;
		}
		
		switch(path) {
		case "/user/getAccount":
			try {
				request.getSession().setAttribute("account", DB.getAccount(id));
				response.sendRedirect("transactions");
			} catch (DatabaseException e) {
	    		DatabaseException.handleException(e, session, response, "accounts");
			}
			break;
		case "/admin/getAccount":
			try {
				request.getSession().setAttribute("account", DB.getAccount(id));
				response.sendRedirect("accountinfo");
			} catch (DatabaseException e) {
	    		DatabaseException.handleException(e, session, response, "accounts");
			}
			break;
		case "/admin/getUser":
    		try {
				request.getSession().setAttribute("user", DB.getUser(id));
	    		response.sendRedirect("accounts");
			} catch (DatabaseException e) {
	    		DatabaseException.handleException(e, session, response, "users");
			}    		
		}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
