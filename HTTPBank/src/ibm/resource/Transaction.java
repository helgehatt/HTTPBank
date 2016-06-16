package ibm.resource;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* FIELDS */
	private int userId;
	private int accountId; //Required

	private long date; // Required
	private String description; // Required
	private double amount; // Required
	private String currency; // Required
	
	private static final transient SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	private static final transient DecimalFormat DECIFORMAT = new DecimalFormat("#0.00");
	
	/* CONSTRUCTORS */
	
	public Transaction(int accountId, Timestamp date, String description, double amount) {
		this.accountId = accountId;
		this.date = date.getTime();
		this.description = description;
		this.amount = amount;
	}
	
	public Transaction(int userId, int accountId, Timestamp date, String description, double amount, String currency) {
		this(accountId, date, description, amount);
		this.userId = userId;
		this.currency = currency;		
	}
	
	/* GETTERS */

	public int getUserId() {
		return userId;
	}
	
	public int getAccountId() {
		return accountId;
	}

	public long getDateRaw() {
		return date;
	}
	
	public String getDate() {
		return FORMAT.format(date);
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getAmount() {
		return DECIFORMAT.format(amount);
	}
	
	public String getCurrency() {
		return currency;
	}
}
