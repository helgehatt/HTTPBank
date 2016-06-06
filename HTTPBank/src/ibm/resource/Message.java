package ibm.resource;

import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String message;
	private long date;
	private String senderName;
	private int userID;
	
	private static final transient SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public Message(String message, Date date, String senderName, int userID) {
		this.message = message;
		this.date = date.getTime();
		this.senderName = senderName;
		this.userID = userID;
	}
	//getters:
	public String getMessage() {
		return message;
	}
	
	public long getDateRaw() {
		return date;
	}
	
	public Date getDate() {
		return new Date(date);
	}
	
	public String getDateAsString() {
		return FORMAT.format(date);
	}
	
	public String getSenderName() {
		return senderName;
	}
	
	public int getUserID() {
		return userID;
	}
	
	
	//Setters:
	public void setDate(String date) throws ParseException {
		this.date = FORMAT.parse(date).getTime();
	}
	
	public void setDate(Date date) {
		this.date = date.getTime();
	}
	
	public void setDate(long date) {
		this.date = date;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
