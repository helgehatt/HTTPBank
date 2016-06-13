package ibm.resource;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DatabaseException extends SQLException {
	private static final long serialVersionUID = 1L;
	
	private String sqlState = "";
	private int errorCode = 0;
	
	public DatabaseException() {
		super("Database Error");
	}
	
	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, int errorCode, String sqlState) {
		this(message);
		this.errorCode = errorCode;
		this.sqlState = sqlState;
	}
	
	@Override
	public int getErrorCode() {
		return errorCode;
	}
	
	@Override
	public String getSQLState() {
		return sqlState;
	}
	
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
}
