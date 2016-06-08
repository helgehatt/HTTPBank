package ibm.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;
import ibm.db.DB.USER;
import ibm.resource.User;

@WebServlet("/user/doUserUpdate")
public class UserInfoChanger extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HashMap<String, String> errors = new HashMap<String, String>();
    	HttpSession session = request.getSession();
    	User user = (User) session.getAttribute("user");
    	
    	String cPassword = request.getParameter("c-password");
    	String nUsername = request.getParameter("username");
    	String nPassword = request.getParameter("n-password");
    	String rPassword = request.getParameter("r-password");
    	    	
    	if (DB.checkLogin(user.getUsername(), cPassword) < 1) {
    		errors.put("cpassword", "Incorrect password");
    	}
    	
    	if (!nPassword.equals(rPassword)) {
    		errors.put("rpassword", "Passwords do not match");
    	}
    	
    	if (errors.isEmpty()) {    	
    		int id = user.getId();
    		
    		if (!nUsername.isEmpty()) {
    			DB.updateUser(id, nUsername, USER.USERNAME);
    		}
    		
    		if (!nPassword.isEmpty()) {
    			DB.updateUser(id, nPassword, USER.PASSWORD);
    		}

			session.setAttribute("user", DB.getUser(id));
        	response.sendRedirect("userinfo");
    	} else {
    		session.setAttribute("errors", errors);
    		response.sendRedirect("changeinfo");
    	}  	  	
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.sendRedirect(request.getContextPath());
    }
}
