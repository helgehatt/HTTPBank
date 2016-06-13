package ibm.resource;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* FIELDS */
	private Long transaction_id; //Only for identification. Not really required here.
	private int account_id; //Required

	private long date; // Required
	private String description; // Required
	private double amount; // Required
	
	private static final transient SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy");
	
	/* CONSTRUCTORS */
	public Transaction(Long transaction_id, int account_id, Timestamp date, String description, double amount) {
		this.transaction_id = transaction_id;
		this.account_id = account_id;
		this.date = date.getTime();
		this.description = description;
		this.amount = amount;
	}
	
	// Keep until DB is fully operational
	// Method made in class DB to replace this for creating new transactions in the database.
	public Transaction(String description, double amount) {
		this.date = new java.util.Date().getTime();
		this.description = description;
		this.amount = amount;
	}
	
	/* GETTERS */
	public Long getId(){
		return transaction_id;
	}
	
	public int getAccountId(){
		return account_id;
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
		return new DecimalFormat("#0.00").format(amount);
	}
}
