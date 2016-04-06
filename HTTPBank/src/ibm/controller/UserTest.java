package ibm.controller;

import java.util.ArrayList;

import ibm.resource.Account;
import ibm.resource.Transaction;
import ibm.resource.User;

public class UserTest {
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private User user;

	public UserTest() {
		
		String date = "16.03.2016";
		String description = "Dogn Ghetto";
		double amount = 1000.12999;
		
		for (int i = 1; i < 5; i++) {
			transactions.add(new Transaction(date, description, amount));
		}
		
		for (int i = 1; i < 5; i++) {
			transactions.add(new Transaction(description, amount));
		}
		
		String type = "Savings Account";
		String bank = "Danske bank";
		String number = "3330.23.12345";
		String currency = "DKK";
		double interest = 1.5;
		double balance = 1500.00;
		String iban = "DK213354355";
		String bic = "DKKASDFAS";
		
		accounts.add(new Account(type, bank, number, currency, interest));
		accounts.add(new Account(type, bank, number, currency, interest, balance));
		accounts.add(new Account(type, bank, number, currency, interest, iban, bic));
		accounts.add(new Account(type, bank, number, currency, interest, transactions));

		
		user = new User("Username", accounts);
		
	}

	public User getUser() {
		return user;
	}
	
}
