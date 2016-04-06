package ibm.resource;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/* FIELDS */

	private String type; // Required
	private String bank; // Required
	private String number; // Required
	private String currency; // Required
	private double interest; // Required
	private double balance; // Default: 0,00
	private String iban; // bic => iban
	private String bic; // iban => bic
	
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // Default: None
	
	/* CONSTRUCTORS */
	
	// Required
	public Account(String type, String bank, String number, String currency, double interest) {
		this.type = type;
		this.bank = bank;
		this.number = number;
		this.currency = currency;
		this.interest = interest;
	}
	
	// Required and balance
	public Account(String type, String bank, String number, String currency, double interest, double balance) {
		this(type, bank, number, currency, interest);
		this.balance = balance;
	}
	
	// Required and transactions
	public Account(String type, String bank, String number, String currency, double interest, ArrayList<Transaction> transactions) {
		this(type, bank, number, currency, interest);
		this.transactions = transactions;
	}
	
	// Required, balance and transactions
	public Account(String type, String bank, String number, String currency, double interest, double balance, ArrayList<Transaction> transactions) {
		this(type, bank, number, currency, interest, balance);
		this.transactions = transactions;
	}
	
	// Required, iban and bic
	public Account(String type, String bank, String number, String currency, double interest, String iban, String bic) {
		this(type, bank, number, currency, interest);
		this.iban = iban;
		this.bic = bic;
	}
	
	// Required, balance, iban and bic
	public Account(String type, String bank, String number, String currency, double interest, double balance, String iban, String bic) {
		this(type, bank, number, currency, interest, balance);
		this.iban = iban;
		this.bic = bic;
	}

	// Required, iban, bic and transactions
	public Account(String type, String bank, String number, String currency, double interest, String iban, String bic, ArrayList<Transaction> transactions) {
		this(type, bank, number, currency, interest, iban, bic);
		this.transactions = transactions;
	}
	
	// Required, balance, iban, bic and transactions
	public Account(String type, String bank, String number, String currency, double interest, double balance, String iban, String bic, ArrayList<Transaction> transactions) {
		this(type, bank, number, currency, interest, balance, iban, bic);
		this.transactions = transactions;
	}
	
	/* GETTERS AND SETTERS */
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getInterest() {
		return Double.toString(interest)+"%";
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
	
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
}
