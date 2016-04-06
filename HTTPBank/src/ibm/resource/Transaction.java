package ibm.resource;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Serializable {
	private static final long serialVersionUID = 1L;

	private String date; // Default: Today
	private String description; // Required
	private double amount; // Required
	
	public Transaction(String description, double amount) {
		this.date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		this.description = description;
		this.amount = amount;
	}
	
	public Transaction(String date, String description, double amount) {
		this.date = date;
		this.description = description;
		this.amount = amount;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAmount() {
		return new DecimalFormat("#0.00").format(amount);
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
