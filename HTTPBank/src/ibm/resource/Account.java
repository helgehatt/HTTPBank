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
	private ArrayList<Transaction> archive		= null;
	
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
		transactions = DB.getTransactions(accountId);
		return transactions;
	}
	
	public ArrayList<Transaction> getArchive() throws DatabaseException {
		archive = DB.getArchive(accountId);
		return archive;
	}
	
	
	/* SETTERS */
	public void setType(String type) {
		this.type = type;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public void setInterest(double interest) {
		this.interest = interest;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
}
