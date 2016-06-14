package ibm.resource;

import java.sql.SQLException;

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
}
