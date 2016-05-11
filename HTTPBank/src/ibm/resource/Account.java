package ibm.resource;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* FIELDS */

	private String type; // Required
	private String number; // Required
	private String iban; // Required
	private String currency; // Required
	private double interest; // Required
	private double balance; // Default: 0,00
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // Default: None
	
	/* CONSTRUCTORS */
	
	// Required
	public Account(String type, String number, String iban, String currency, double interest) {
		this.type = type;
		this.number = number;
		this.iban = iban;
		this.currency = currency;
		this.interest = interest;
	}
	
	// Required and balance
	public Account(String type, String number, String iban, String currency, double interest, double balance) {
		this(type, number, iban, currency, interest);
		this.balance = balance;
	}
	
	// Required and transactions
	public Account(String type, String number, String iban, String currency, double interest, ArrayList<Transaction> transactions) {
		this(type, number, iban, currency, interest);
		this.transactions = transactions;
	}
	
	// Required, balance and transactions
	public Account(String type, String number, String iban, String currency, double interest, double balance, ArrayList<Transaction> transactions) {
		this(type, number, iban, currency, interest, balance);
		this.transactions = transactions;
	}
	
	/* GETTERS AND SETTERS */
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getInterest() {
		return Double.toString(interest) + "%";
	}
	
	public void setInterest(double interest) {
		this.interest = interest;
	}
	
	public String getBalance() {
		return new DecimalFormat("#0.00").format(balance);
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
}
