package ibm.controller;

import ibm.resource.InputException;

public class AttributeChecks {
	//User Methods
	/**
	 * Checks if the given string is a valid cpr-number.
	 */
	public static void checkCpr(String cpr) throws InputException {
    	if (cpr.length() < 5)
    		throw new InputException();
    }
    
	/**
	 * Checks if the given string is a valid name.
	 */
	public static void checkName(String name) throws InputException {
    	if (name.length() < 5) 
    		throw new InputException();    	
    }
    
	/**
	 * Checks if the given string is a valid institute.
	 */
	public static void checkInstitute(String institute) throws InputException {
    	if (institute.length() < 5)
    		throw new InputException();
    }
    
	/**
	 * Checks if the given string is a valid consultant.
	 */
	public static void checkConsultant(String consultant) throws InputException {
    	if (consultant.length() < 5)
    		throw new InputException();
    }
	
	//Account Methods
	/**
	 * Checks if the given string is a valid type.
	 */
	public static void checkType(String type) throws InputException {
    	if (type.length() < 5)
    		throw new InputException();
    }
    
	/**
	 * Checks if the given string is a valid number.
	 */
	public static void checkNumber(String number) throws InputException {
    	if (number.length() < 5)
    		throw new InputException();
    }
    
	/**
	 * Checks if the given string is a valid iban.
	 */
	public static void checkIban(String iban) throws InputException {
    	if (iban.length() < 5)
    		throw new InputException();
    }
    
	/**
	 * Checks if the given string is a valid currency.
	 */
	public static void checkCurrency(String currency) throws InputException {
    	if (currency.length() != 3)
    		throw new InputException();
    }
    
	/**
	 * Checks if the given string is a valid interest.
	 */
	public static void checkInterest(String interest) throws InputException {
    	try {
    		Double.parseDouble(interest);
    	} catch (NumberFormatException e) {
    		throw new InputException();
    	}
    }
    
	/**
	 * Checks if the given string is a valid balance.
	 */
	public static void checkBalance(String balance) throws InputException {
    	try {
    		Double.parseDouble(balance);
    	} catch (NumberFormatException e) {
    		throw new InputException();
    	}
    }
}
