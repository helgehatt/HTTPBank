package ibm.controller;

import java.util.ArrayList;

import ibm.resource.Account;
import ibm.resource.Transaction;
import ibm.resource.User;

public class AdminTest {
	private ArrayList<Account> accounts = new ArrayList<Account>();
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	private ArrayList<User> users = new ArrayList<User>();

	public AdminTest() {
		
		String date = "16.03.2016";
		String description = "Dogn Ghetto";
		double amount = 145.34;
		
		for (int i = 1; i < 7; i++) {
			transactions.add(new Transaction(date, description, i + amount));
		}
		
		String type = "Savings Account";
		String bank = "Danske bank";
		String number = "3330.23.1234";
		String currency = "DKK";
		double interest = 1.5;
		double balance = 1500.00;
		String iban = "DK12343214324";
		String bic = "DKKASDF";
		
		for (int i = 1; i < 4; i++) {
			accounts.add(new Account(type, bank, number+i, currency, interest, balance, iban, bic, transactions));
		}
		
		String name = "User ";
		
		for (int i = 1; i < 6; i++) {
			users.add(new User(name + i, accounts));
		}
		
	}

	public ArrayList<User> getUsers() {
		return users;
	}
	
}
