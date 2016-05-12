package ibm.resource;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	/* FIELDS */

	private String cpr; // Required
	private String name; // Required
	private String institute; // Required?
	private String consultant; // Required?
	private ArrayList<Account> accounts = new ArrayList<Account>(); // Default: None

	/* CONSTRUCTORS */

	public User(String cpr, String name) {
		this.cpr = cpr;
		this.name = name;
	}

	public User(String cpr, String name, ArrayList<Account> accounts) {
		this(cpr, name);
		this.accounts = accounts;
	}
	
	public User(String cpr, String name, String institute, String consultant) {
		this(cpr, name);
		this.institute = institute;
		this.consultant = consultant;
	}

	/* GETTERS AND SETTERS */

	public String getCpr() {
		return cpr;
	}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	public String getConsultant() {
		return consultant;
	}

	public void setConsultant(String consultant) {
		this.consultant = consultant;
	}

	public ArrayList<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
}
