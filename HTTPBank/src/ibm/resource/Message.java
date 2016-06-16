package ibm.resource;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String text;
	private String preview;
	private long date;
	private String senderName;
	private int userID;
	
	private static final transient SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	public Message(String text, Date date, String senderName, int userID) {
		this.text = text;
		this.date = date.getTime();
		this.senderName = senderName;
		this.userID = userID;
		
		if (text.length() > 30)
			preview = text.substring(0, 30) + "...";
	}
	//getters:
	public String getText() {
		return text;
	}
	
	public String getPreview() {
		return preview;
	}
	
	public long getDateRaw() {
		return date;
	}
	
	public String getDate() {
		return FORMAT.format(date);
	}
	
	public String getSenderName() {
		return senderName;
	}
	
	public int getUserID() {
		return userID;
	}	
}
