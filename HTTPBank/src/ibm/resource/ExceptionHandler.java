package ibm.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ExceptionHandler {

	public static void success(String confirmation, HttpSession session) {
		session.setAttribute("confirmation", confirmation);
	}
	
	public static void failure(String exception, HttpSession session) {
 		session.setAttribute("exception", exception);
	}
	
	public static void failure(String exception, HttpSession session, HttpServletResponse response, String redirectUrl) throws IOException {
		failure(exception, session);
 		response.sendRedirect(redirectUrl);
	}
	
	public static void failure(DatabaseException e, String exception, HttpSession session) {
		if (e.getSQLState().equals("08008"))
			exception = "Insufficient funds.";
		else
			e.printStackTrace();
		failure(exception, session);
	}
	
	public static void failure(DatabaseException e, String exception, HttpSession session, HttpServletResponse response, String redirectUrl) throws IOException {
		failure(e, exception, session);
 		response.sendRedirect(redirectUrl);
	}
}
