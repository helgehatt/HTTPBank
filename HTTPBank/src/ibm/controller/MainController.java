package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/logout", 
		
		"/user/accounts", "/user/transactions", "/user/accountinfo", 
		"/user/transfer", "/user/international", "/user/archive", 
		"/user/userinfo", "/user/changeinfo", "/user/inbox", 
		
		"/admin/users", "/admin/newuser", 
		"/admin/accounts", "/admin/newaccount", 
		"/admin/accountinfo", "/admin/transactions", 
		"/admin/editaccount", "/admin/closeaccount", "/admin/archive",
		"/admin/transfer", "/admin/international", "/admin/depositwithdrawal", 
		"/admin/userinfo", "/admin/edituser", "/admin/resetpassword", "/admin/deleteuser" })

public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);

		String path = request.getRequestURI().replace(request.getContextPath(), "");

		if (session.getAttribute("admin") == null) {
			response.sendError(401);
			return;
		} else {
			boolean isAdmin = (boolean) session.getAttribute("admin");
			String prefix = path.substring(0, path.lastIndexOf("/"));
			if (prefix.equals("/user") && isAdmin || prefix.equals("/admin") && !isAdmin) {
				response.sendError(403);
				return;
			}
		}		
		
		switch (path) {
		case "/logout":
			session.invalidate();
			request.getRequestDispatcher("login.jsp").forward(request, response);
			break;			
		case "/user/accounts":
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
		case "/user/changeinfo":
			request.getRequestDispatcher("../WEB-INF/jsp/user/change-info.jsp").forward(request, response);
			break;
		case "/user/inbox":
			request.getRequestDispatcher("../WEB-INF/jsp/user/inbox.jsp").forward(request, response);
			break;
		case "/admin/users":
			session.setAttribute("user", null);
			session.setAttribute("archive", null);
			request.getRequestDispatcher("../WEB-INF/jsp/admin/user-list.jsp").forward(request, response);
			break;
		case "/admin/newuser":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/new-user.jsp").forward(request, response);
			break;
		case "/admin/accounts":
			session.setAttribute("account", null);
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
		case "/admin/resetpassword":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/reset-password.jsp").forward(request, response);
			break;
		case "/admin/deleteuser":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/delete-user.jsp").forward(request, response);
			break;
		case "/admin/archive": 
			request.getRequestDispatcher("../WEB-INF/jsp/admin/archive.jsp").forward(request, response);
			break;
		}
		
	}
}
