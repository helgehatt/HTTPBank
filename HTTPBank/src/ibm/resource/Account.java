package ibm.resource;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import ibm.db.DB;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final DecimalFormat FORMAT = new DecimalFormat("#0.00");
	
	/* FIELDS */
	private int userId; //Required
	private int accountId; //Required
	private String name; //Required
	
	private String type; // Required
	private String number; // Required
	private String iban; // Required
	private double interest; // Required
	private double balance; // Default: 0,00
	private String currency; // Required
	private ArrayList<Transaction> transactions = null; // Default: None
	
	/* CONSTRUCTORS */
	// Required and balance
	public Account(int userId, int accountId, String name, String type, String number, String iban, String currency, double interest, double balance) {
		this.userId = userId;
		this.accountId = accountId;
		this.name = name;
		this.type = type;
		this.number = number;
		this.iban = iban;
		this.currency = currency;
		this.interest = interest;
		this.balance = balance;
	}

	/* GETTERS */
	public int getUserId() {
		return userId;
	}
	
	public int getId() {
		return accountId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getNumber() {
		return number;
	}
	
	public String getIban() {
		return iban;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public String getInterest() {
		return FORMAT.format(interest) + "%";
	}
	
	public String getBalance() {
		return FORMAT.format(balance);
	}
	
	/*
	 * Returns a list of Transactions related to this Account.
	 * Should query whenever transactions are updated.
	 */
	public ArrayList<Transaction> getTransactions() throws DatabaseException {
		if (transactions == null)
			transactions = DB.getTransactions(accountId);
		return transactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountId != other.accountId)
			return false;
		return true;
	}
}
