package ibm.controller;

import ibm.resource.InputException;

public class AttributeChecks {
	//User Methods
	/**
	 * Checks if the given string is a valid cpr-number.
	 */
	public static void checkCpr(String cpr) throws InputException {
    	cpr = cpr.replace("-", "");
		if (cpr.length() != 9)
    		throw new InputException("Please insert 9 digits.");
    	if (!isNumerous(cpr))
    		throw new InputException("Please insert numbers only.");
    }
    
	/**
	 * Checks if the given string is a valid name.
	 */
	public static void checkName(String name) throws InputException {
		if (name.length() == 0)
    		throw new InputException("Please insert name.");   
    	if (!isAlphabetic(name)) 
    		throw new InputException("Please insert letters only.");    	
    }
    
	/**
	 * Checks if the given string is a valid institute.
	 */
	public static void checkInstitute(String institute) throws InputException {
		// TODO: check if institute in DB
    }
    
	/**
	 * Checks if the given string is a valid consultant.
	 */
	public static void checkConsultant(String consultant) throws InputException {
		// TODO: check if consultant in DB
    }
	
	//Account Methods
	/**
	 * Checks if the given string is a valid type.
	 */
	public static void checkType(String type) throws InputException {
		if (type.length() == 0)
			throw new InputException("Please insert type.");
    }
    
	/**
	 * Checks if the given string is a valid number.
	 */
	public static void checkNumber(String number) throws InputException {
		if (number.length() != 9)
			throw new InputException("Please insert 9 digits.");
		if (!isNumerous(number))
			throw new InputException("Please insert numbers only.");
		// TODO: check if number exists in DB?
    }
    
	/**
	 * Checks if the given string is a valid iban.
	 */
	public static void checkIban(String iban) throws InputException {
		if (iban.length() < 5)
			throw new InputException("Please insert more than 4 characters.");
		if (!isAlphabetic(iban.substring(0, 1)))
			throw new InputException("First and second character must be letters.");
		if (!isNumerous(iban.substring(2, 3)))
			throw new InputException("Third and fourth character must be digits.");
		// TODO: check if iban exists in DB?
    }
	
	/**
	 * Checks if the given string is a valid bic.
	 * @param bic
	 * @throws InputException
	 */
	public static void checkBic(String bic) throws InputException {
		if (bic.length() != 8)
			throw new InputException("Please insert 8 characters.");
	}
    
	/**
	 * Checks if the given string is a valid currency.
	 */
	public static void checkCurrency(String currency) throws InputException {
    	if (currency.length() != 3)
    		throw new InputException("Please insert 3 letters.");
    	// TODO: check if currency exists in DB
    }
    
	/**
	 * Checks if the given string is a valid interest.
	 * @return The string parsed to double.
	 */
	public static double checkInterest(String interest) throws InputException {
		if (interest.length() == 0)
			throw new InputException("Please insert interest rate.");
		double value;
    	try {
    		interest = interest.replace("%", "");
    		interest = interest.replace(",", ".");
    		value = Double.parseDouble(interest);
    	} catch (NumberFormatException e) {
    		throw new InputException("Please insert a number.");
    	}
    	return value;
    }
    
	/**
	 * Checks if the given string is a valid balance.
	 * @return The string parsed to double.
	 */
	public static double checkBalance(String balance) throws InputException {
		if (balance.length() == 0)
			balance = "0";
		double value;
    	try {
    		balance = balance.replace(",", ".");
    		value = Double.parseDouble(balance);
    	} catch (NumberFormatException e) {
    		throw new InputException("Please insert a number.");
    	}
    	return value;
    }
	/**
	 * Checks if the given string is a valid amount.
	 * @param amount
	 * @return The string parsed to double.
	 * @throws InputException
	 */
	public static double checkAmount(String amount) throws InputException {
		if (amount.length() == 0)
			throw new InputException("Please insert an amount.");
		double value;
    	try {
    		amount = amount.replace(",", ".");
    		value = Double.parseDouble(amount);
    	} catch (NumberFormatException e) {
    		throw new InputException("Please insert a number.");
    	}
    	return value;
    }
	
	private static boolean isAlphabetic(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isAlphabetic(string.charAt(i)))
				return false;
		}
		return true;
	}
	
	private static boolean isNumerous(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isDigit(string.charAt(i)))
				return false;
		}
		return true;
		
	}
}
