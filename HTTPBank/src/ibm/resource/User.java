package ibm.resource;

import java.io.Serializable;
import java.util.ArrayList;

import ibm.db.DB;

public class User implements Serializable, Comparable<User> {
	private static final long serialVersionUID = 1L;

	/* FIELDS */
	private int userId; //Required
	private String username; //Required
	//Password is required in database, but is never made a variable here.
	
	private String cpr; // Required
	private String name; // Required
	private String institute; // Required?
	private String consultant; // Required?

	/* CONSTRUCTORS */
	public User(int userId, String cpr, String name) {
		this.userId = userId;
		this.cpr = cpr;
		this.name = name;
	}
	
	public User(int userId, String username, String cpr, String name, String institute, String consultant) {
		this(userId, cpr, name);
		this.username = username;
		this.institute = institute;
		this.consultant = consultant;
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
	 * Should query whenever accounts are updated.
	 */
	public ArrayList<Account> getAccounts() throws DatabaseException {
		return DB.getAccounts(userId);
	}
	
	public ArrayList<Message> getMessages() throws DatabaseException {
		return DB.getMessages(userId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
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
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public int compareTo(User user) {
		return name.compareTo(user.getName());
	}
}
