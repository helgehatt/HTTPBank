package ibm.test;

import java.util.ArrayList;
import java.util.Date;

import ibm.resource.Account;
import ibm.resource.Transaction;
import ibm.resource.User;

public final class TestData {
	private static ArrayList<Account> accounts;
	private static ArrayList<Transaction> transactions;
	private static ArrayList<User> users;
	
	private TestData() {
		
	}
	
	private static void init() {
		@SuppressWarnings("deprecation")
		long date = new Date(2016, 03, 16).getTime();
		String description = "Dogn Ghetto";
		double amount = 65.239;
		
		String type = "Savings Account";
		String number = "3330.23.12";
		String iban = "DK213354";
		String currency = "";
		String dkk = "DKK";
		String eur = "EUR";
		String nok = "NOK";
		double interest = 1.5;
		double balance = 1500.00;
		
		String cpr = "010192-123";
		String name = "User";
		
		users = new ArrayList<User>();
		
		for (int i = 1; i <= 8; i++) {
			accounts = new ArrayList<Account>();
			
			for (int j = 1; j <= 3; j++) {
				
				if (j % 2 == 0) {
					currency = nok;
					amount = amount * 1.25;
					balance = balance * 1.25;
				} else if (j % 3 == 0) {
					currency = eur;
					amount = amount / 8;
					balance = balance / 8;
				} else {
					currency = dkk;
					amount = 65.239;
					balance = 1500;
				}
				
				transactions = new ArrayList<Transaction>();
				for (int k = 1; k <= 10; k++) {
					transactions.add(new Transaction(i*10+k, i, date, description, amount + k));
				}
				
				accounts.add(new Account(1, i, "Account"+i, type, number + i + j, iban + i + j, currency, interest, balance + j * 100, transactions));
			
			}
			
			users.add(new User(1, name + i, cpr + i, name +" "+ i, "HTTPBank", accounts));
		}
	}

	public static ArrayList<User> getUsers() {
		if (users == null)
			init();
		return users;
	}
	
	public static User getUser() {
		if (users == null)
			init();
		return users.get(0);
	}
	
	public static User getUserByCpr(String cpr) {
		if (users == null)
			init();
		for (User user : users) {
			if (user.getCpr().equals(cpr))
				return user;
		}
		return null;
	}
	
	public static Account getAccountByIban(String iban) {
		if (accounts == null)
			init();
		for (User user : users)
			for (Account account : user.getAccounts()) {
				if (account.getIban().equals(iban))
					return account;
			}
		return null;
	}
	
}
