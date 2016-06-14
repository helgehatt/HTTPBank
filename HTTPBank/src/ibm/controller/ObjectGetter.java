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
import ibm.resource.Account;
import ibm.resource.AttributeChecks;
import ibm.resource.DatabaseException;
import ibm.resource.ExceptionHandler;
import ibm.resource.InputException;
import ibm.resource.User;

@WebServlet(urlPatterns = { "/user/getAccount", "/user/getArchive",
							"/admin/getAccount", "/admin/getArchive", 
							"/admin/getUser", "/admin/getMoreUsers",
							"/admin/findUsers" })
public class ObjectGetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();        
        
		String path = request.getRequestURI().replace(request.getContextPath(), "");
		
		switch(path) {
		case "/admin/findUsers":
			try {
				session.setAttribute("users", DB.searchUsers(request.getParameter("search")));
			} catch (DatabaseException e) {
				ExceptionHandler.failure("Failed searching for users.", session);
			}
			response.sendRedirect("users");
			return;
		case "/admin/getArchive":
		case "/user/getArchive":
			try {
				long from = AttributeChecks.checkDate(request.getParameter("from"));
				long to = AttributeChecks.checkDate(request.getParameter("to"));
				int id = ((Account) session.getAttribute("account")).getId();
				session.setAttribute("archive", DB.searchArchive(id, "" + from, "" + to));
			} catch (InputException e) {
	        	session.setAttribute("error", e.getMessage());
			} catch (DatabaseException e) {
				ExceptionHandler.failure("Failed searching the archive.", session);	
			}
			response.sendRedirect("archive");
			return;
		}
		
        int id = 0;
        
        try {
        	id = (int) Double.parseDouble(request.getParameter("id"));
		} catch (NumberFormatException e) {
			ExceptionHandler.failure("Failed parsing the ID.", session);
    		if (path.equals("/admin/getUser"))
    			response.sendRedirect("users");
    		else
    			response.sendRedirect("accounts");
			return;
		}
		
		switch(path) {
		case "/user/getAccount":
			try {
				session.setAttribute("account", DB.getAccount(id));
				response.sendRedirect("transactions");
			} catch (DatabaseException e) {
				ExceptionHandler.failure("Failed getting the account.", session, response, "accounts");
			}
			break;
		case "/admin/getAccount":
			try {
				session.setAttribute("account", DB.getAccount(id));
				response.sendRedirect("accountinfo");
			} catch (DatabaseException e) {
				ExceptionHandler.failure("Failed getting the account.", session, response, "accounts");
			}
			break;
		case "/admin/getUser":
    		try {
				session.setAttribute("user", DB.getUser(id));
	    		response.sendRedirect("accounts");
			} catch (DatabaseException e) {
				ExceptionHandler.failure("Failed getting the user.", session, response, "users");
			}
    		break;
		case "/admin/getMoreUsers":
    		try {
    			@SuppressWarnings("unchecked")
				ArrayList<User> users = (ArrayList<User>) session.getAttribute("users");
    			users.addAll(DB.getUsers(id));
				session.setAttribute("users", users);
	    		response.sendRedirect("users");
			} catch (DatabaseException e) {
				ExceptionHandler.failure("Failed getting more users.", session, response, "users");
			}
    		break;
		}
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
