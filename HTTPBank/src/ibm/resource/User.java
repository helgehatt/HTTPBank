package ibm.resource;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long userId;
	private String username;
	
	private ArrayList<Account> accounts = null;
	
	public User(long userId, String username) {
		this.userId = userId;
		this.username = username;
	}
	
	public User(long userId, String username, ArrayList<Account> accounts) {
		this.userId = userId;
		this.username = username;
		this.accounts = accounts;
	}
	
	public User(String username, ArrayList<Account> accounts) {
		this.username = username;
		this.accounts = accounts;
	}

	public long getUserId(){
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
}
