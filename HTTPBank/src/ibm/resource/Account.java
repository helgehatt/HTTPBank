package ibm.resource;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* FIELDS */
	private int user_id; //Required
	private int account_id; //Required
	private String name; //Required
	
	private String type; // Required
	private String number; // Required
	private String iban; // Required
	private double interest; // Required
	private double balance; // Default: 0,00
	private String currency; // Required
	private ArrayList<Transaction> transactions = null; // Default: None
	
	/* CONSTRUCTORS */
	
	// Required
	public Account(int user_id, int account_id, String name, String type, String number, String iban, String currency, double interest) {
		this.user_id = user_id;
		this.account_id = account_id;
		this.type = type;
		this.number = number;
		this.iban = iban;
		this.currency = currency;
		this.interest = interest;
	}
	
	// Required and balance
	public Account(int user_id, int account_id, String name, String type, String number, String iban, String currency, double interest, double balance) {
		this(user_id, account_id, name, type, number, iban, currency, interest);
		this.balance = balance;
	}
	
	// Required and transactions
	public Account(int user_id, int account_id, String name, String type, String number, String iban, String currency, double interest, ArrayList<Transaction> transactions) {
		this(user_id, account_id, name, type, number, iban, currency, interest);
		this.transactions = transactions;
	}
	
	// Required, balance and transactions
	public Account(int user_id, int account_id, String name, String type, String number, String iban, String currency, double interest, double balance, ArrayList<Transaction> transactions) {
		this(user_id, account_id, name, type, number, iban, currency, interest, balance);
		this.transactions = transactions;
	}
	
	/* GETTERS */
	public int getUserId() {
		return user_id;
	}
	
	public int getId() {
		return account_id;
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
		return Double.toString(interest) + "%";
	}
	
	public String getBalance() {
		return new DecimalFormat("#0.00").format(balance);
	}
	
	/*
	 * Returns a list of Transactions related to this Account.
	 * TODO: Should query database for transactions if called when null?
	 */
	public ArrayList<Transaction> getTransactions() {
		return transactions;
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
