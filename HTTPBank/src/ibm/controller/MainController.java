package ibm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/user/accounts", "/user/transfer", "/user/transactions", 
		"/admin/users", "/admin/accounts", "/admin/transactions", "/admin/info", "/admin/transfer", "/admin/international" })
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String path = request.getRequestURI().replace(request.getContextPath(), "");

        // TODO: DB Authentication: Check login status.
		
		switch (path) {
		case "/user/accounts":
			request.getRequestDispatcher("../WEB-INF/jsp/user/account-list.jsp").forward(request, response);
			break;
		case "/user/transfer":
			request.getRequestDispatcher("../WEB-INF/jsp/user/transfer.jsp").forward(request, response);
			break;
		case "/user/transactions":
			request.getRequestDispatcher("../WEB-INF/jsp/user/transaction-list.jsp").forward(request, response);
			break;
		case "/admin/users":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/user-list.jsp").forward(request, response);
			break;
		case "/admin/accounts":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/account-list.jsp").forward(request, response);
			break;
		case "/admin/transactions":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/transaction-list.jsp").forward(request, response);
			break;
		case "/admin/info":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/account-info.jsp").forward(request, response);
			break;
		case "/admin/transfer":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/transfer-dom.jsp").forward(request, response);
			break;
		case "/admin/international":
			request.getRequestDispatcher("../WEB-INF/jsp/admin/transfer-int.jsp").forward(request, response);
			break;
		}
	}
}
