package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ibm.db.DB;

@WebServlet(urlPatterns = { "/login", 
		
		"/user/accounts", "/user/transactions", "/user/accountinfo", 
		"/user/transfer", "/user/international", "/user/archive", 
		"/user/userinfo", "/user/inbox", 
		
		"/admin/users", "/admin/search", "/admin/newuser", "/admin/accounts", "/admin/newaccount", 
		"/admin/transactions", "/admin/accountinfo", "/admin/depositwithdrawal", "/admin/editaccount", "/admin/closeaccount",
		"/admin/transfer", "/admin/international", "/admin/userinfo", "/admin/edituser", "/admin/archive" })
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String path = request.getRequestURI().replace(request.getContextPath(), "");

        // TODO: DB Authentication: Check login status.
				
		
		switch (path) {
		case "/login":
			HttpSession session = request.getSession(false);
			if (session != null) session.invalidate();
			request.getRequestDispatcher("login.jsp").forward(request, response);
			break;			
		case "/user/accounts":
			//request.getSession().setAttribute("user", DB.getUser(1));
			request.getRequestDispatcher("../WEB-INF/jsp/user/account-list.jsp").forward(request, response);
			break;
		case "/user/transfer":
			request.getRequestDispatcher("../WEB-INF/jsp/user/transfer-dom.jsp").forward(request, response);
			break;
		case "/user/international":
			request.getRequestDispatcher("../WEB-INF/jsp/user/transfer-int.jsp").forward(request, response);
			break;
		case "/user/transactions":
			request.getRequestDispatcher("../WEB-INF/jsp/user/transaction-list.jsp").forward(request, response);
			break;
		case "/user/accountinfo":
			request.getRequestDispatcher("../WEB-INF/jsp/user/account-info.jsp").forward(request, response);
			break;
		case "/user/archive":
			request.getRequestDispatcher("../WEB-INF/jsp/user/archive.jsp").forward(request, response);
			break;
		case "/user/userinfo":
			request.getRequestDispatcher("../WEB-INF/jsp/user/user-info.jsp").forward(request, response);
			break;
		case "/user/inbox":
			request.getRequestDispatcher("../WEB-INF/jsp/user/inbox.jsp").forward(request, response);
			break;
		case "/admin/users":
			request.getSession().setAttribute("users", DB.getUsers());
			request.getRequestDispatcher("../WEB-INF/jsp/admin/user-list.jsp").forward(request, response);
			break;
		case "/admin/newuser":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/new-user.jsp").forward(request, response);
			break;
		case "/admin/accounts":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/account-list.jsp").forward(request, response);
			break;
		case "/admin/newaccount":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/new-account.jsp").forward(request, response);
			break;
		case "/admin/accountinfo":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/account-info.jsp").forward(request, response);
			break;
		case "/admin/transactions":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/transaction-list.jsp").forward(request, response);
			break;
		case "/admin/editaccount":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/edit-account.jsp").forward(request, response);
			break;
		case "/admin/closeaccount":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/close-account.jsp").forward(request, response);
			break;
		case "/admin/depositwithdrawal":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/deposit-withdrawal.jsp").forward(request, response);
			break;
		case "/admin/transfer":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/transfer-dom.jsp").forward(request, response);
			break;
		case "/admin/international":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/transfer-int.jsp").forward(request, response);
			break;
		case "/admin/userinfo":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/user-info.jsp").forward(request, response);
			break;
		case "/admin/edituser":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/edit-user.jsp").forward(request, response);
			break;
		case "/admin/archive": 
			request.getRequestDispatcher("../WEB-INF/jsp/admin/archive.jsp").forward(request, response);
			break;
		}
		
	}
}
