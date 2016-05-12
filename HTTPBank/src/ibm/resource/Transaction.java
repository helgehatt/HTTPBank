package ibm.resource;

import java.io.Serializable;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* FIELDS */
	private long transaction_id; //Only for identification.
	private int account_id; //Required

	private long date; // Required
	private String description; // Required
	private double amount; // Required
	
	private static final transient SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/* CONSTRUCTORS */

	public Transaction(long transaction_id, int account_id, String description, double amount) {
		this.transaction_id = transaction_id;
		this.account_id = account_id;
		this.date = new java.util.Date().getTime();
		this.description = description;
		this.amount = amount;
	}
	
	public Transaction(long transaction_id, int account_id, long date, String description, double amount) {
		this.transaction_id = transaction_id;
		this.account_id = account_id;
		this.date = date;
		this.description = description;
		this.amount = amount;
	}
	
	public Transaction(long transaction_id, int account_id, Date date, String description, double amount) {
		this.transaction_id = transaction_id;
		this.account_id = account_id;
		this.date = date.getTime();
		this.description = description;
		this.amount = amount;
	}
	
	public Transaction(long transaction_id, int account_id, String date, String description, double amount) throws ParseException {
		this.transaction_id = transaction_id;
		this.account_id = account_id;
		this.date = FORMAT.parse(date).getTime();
		this.description = description;
		this.amount = amount;
	}
	
	/* GETTERS */
	public long getId(){
		return transaction_id;
	}
	
	public int getAccountId(){
		return account_id;
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
	
	public String getDescription() {
		return description;
	}
	
	public String getAmount() {
		return new DecimalFormat("#0.00").format(amount);
	}
	
	/* SETTERS */
	public void setDate(String date) throws ParseException {
		this.date = FORMAT.parse(date).getTime();
	}
	
	public void setDate(Date date) {
		this.date = date.getTime();
	}
	
	public void setDate(long date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
