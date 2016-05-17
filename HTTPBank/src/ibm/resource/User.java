package ibm.resource;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import ibm.db.DB;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/* FIELDS */
	private int userId; //Required
	private String username; //Required
	//Password is required in database, but is never made a variable here.
	
	private String cpr; // Required
	private String name; // Required
	private String institute; // Required?
	private String consultant; // Required?
	
	private ArrayList<Account> accounts = null; // Default: None

	/* CONSTRUCTORS */
	public User(int userId, String username){
		this.userId = userId;
		this.username = username;
	}

	public User(int userId, String username, String cpr, String name) {
		this(userId, username);
		this.cpr = cpr;
		this.name = name;
	}
	
	public User(int userId, String username, String cpr, String name, String institute) {
		this(userId, username, cpr, name);
		this.institute = institute;
	}
	
	public User(int userId, String username, String cpr, String name, String institute, String consultant) {
		this(userId, username, cpr, name, institute);
		this.consultant = consultant;
	}

	public User(int userId, String username, String cpr, String name, String institute, ArrayList<Account> accounts) {
		this(userId, username, cpr, name);
		this.accounts = accounts;
	}
	
	public User(int userId, String username, String cpr, String name, String institute, String consultant, ArrayList<Account> accounts) {
		this(userId, username, cpr, name, institute);
		this.consultant = consultant;
		this.accounts = accounts;
	}
	
	// Keep until DB is fully operational
	// Method made in class DB to replace this for creating new users in the database.
	public User(String cpr, String name, String institute, String consultant) {
		this.cpr = cpr;
		this.name = name;
		this.institute = institute;
		this.consultant = consultant;
		this.accounts = new ArrayList<Account>();
	}

	/* GETTERS */
	public int getId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}

	public String getCpr() {
		return cpr;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInstitute() {
		return institute;
	}
	
	public String getConsultant() {
		return consultant;
	}
	
	/*
	 * Get Accounts related to this User.
	 * TODO: Attempt to query database for accounts if requested when null?
	 * Should query whenever accounts are updated.
	 */
	public ArrayList<Account> getAccounts() {
		try {
			accounts = DB.getAccounts(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	
	/* SETTERS */
	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
}
