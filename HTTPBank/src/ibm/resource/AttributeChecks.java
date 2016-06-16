package ibm.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeChecks {
	//User Methods
	
	public static void checkUserName(String userName) throws InputException {
		if (userName.length() < 4)
			throw new InputException("Please enter at least 4 characters.");
		if (hasWhitespace(userName))
			throw new InputException("Please enter a user name without whitespace.");
	}
	
	/**
	 * Checks if the given string is a valid cpr-number.
	 */
	public static String checkCpr(String cpr) throws InputException {
    	cpr = cpr.replace("-", "");
		if (cpr.length() != 10)
    		throw new InputException("Please enter 10 digits.");
    	if (!isNumerous(cpr))
    		throw new InputException("Please enter numbers only.");
    	return cpr.substring(0, 6) + "-" + cpr.substring(6, 10);
    }
    
	/**
	 * Checks if the given string is a valid name.
	 */
	public static void checkRealName(String name) throws InputException {
		if (name.isEmpty())
    		throw new InputException("Please enter name.");   
    	if (!isAlphabeticWithWhitespace(name)) 
    		throw new InputException("Please enter letters only.");    	
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
	 * Checks if the given string is a valid account name.
	 * @param accountName
	 * @throws InputException
	 */
	public static void checkAccountName(String accountName) throws InputException {
		if (!isAlphabeticWithWhitespace(accountName))
			throw new InputException("Please enter letters only.");
	}
	
	/**
	 * Checks if the given string is a valid type.
	 */
	public static void checkType(String type) throws InputException {
		if (type.isEmpty())
			throw new InputException("Please enter type.");
    }
    
	/**
	 * Checks if the given string is a valid number.
	 */
	public static void checkNumber(String number) throws InputException {
		if (number.length() != 9)
			throw new InputException("Please enter 9 digits.");
		if (!isNumerous(number))
			throw new InputException("Please enter numbers only.");
		// TODO: check if number exists in DB?
    }
    
	/**
	 * Checks if the given string is a valid iban.
	 */
	public static void checkIban(String iban) throws InputException {
		if (hasWhitespace(iban))
			throw new InputException("Please don't enter whitespace.");
		if (iban.length() < 5)
			throw new InputException("Please enter more than 4 characters.");
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
			throw new InputException("Please enter 8 characters.");
	}
    
	/**
	 * Checks if the given string is a valid interest.
	 * @return The string parsed to double.
	 */
	public static double checkInterest(String interest) throws InputException {
		if (interest.isEmpty())
			throw new InputException("Please enter interest rate.");
    	try {
    		return Double.parseDouble(interest.replace(",", ".").replace("%", ""));
    	} catch (NumberFormatException e) {
    		throw new InputException("Please enter a number.");
    	}
    }
    
	/**
	 * Checks if the given string is a valid balance.
	 * @return The string parsed to double.
	 */
	public static double checkBalance(String balance) throws InputException {
		if (balance.isEmpty())
			balance = "0";
    	try {
    		return Double.parseDouble(balance.replace(",", "."));
    	} catch (NumberFormatException e) {
    		throw new InputException("Please enter a number.");
    	}
    }
	/**
	 * Checks if the given string is a valid amount.
	 * @param amount
	 * @return The string parsed to double.
	 * @throws InputException
	 */
	public static double checkAmount(String amount) throws InputException {
		double value = 0;
		if (amount.isEmpty())
			throw new InputException("Please enter an amount.");
    	try {
    		value = Double.parseDouble(amount.replace(",", "."));
    	} catch (NumberFormatException e) {
    		throw new InputException("Please enter a number.");
    	}
    	if (value <= 0)
    		throw new InputException("Please enter a positive number.");
    	return value;
    }
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * Checks if the string is a valid date.
	 * @param date
	 * @return The string parsed to long.
	 * @throws InputException
	 */
	public static long checkDate(String date) throws InputException {
		try {
			return FORMAT.parse(date).getTime();
		} catch (ParseException e) {
			throw new InputException("Please enter both dates.");
		}
		
	}
	
	private static boolean isAlphabetic(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (!Character.isAlphabetic(string.charAt(i)))
				return false;
		}
		return true;
	}
	
	private static boolean isAlphabeticWithWhitespace(String string) {
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			if (!Character.isAlphabetic(ch) && !Character.isWhitespace(ch))
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
	
	private static boolean hasWhitespace(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (Character.isWhitespace(string.charAt(i)))
				return true;
		}
		return false;
	}
	
	// Regular expression for recognizing special characters.
	private static final String ADD_REGEX = "&|\\*|@|\\[|\\]|\\{|\\}|\\\\|\\^|:|=|!|\\/|>|<|-|\\(|\\)|%|\\+|\\?|;|'|~|\\|";
	private static final String REMOVE_REGEX = "\\\\&|\\\\\\*|\\\\@|\\\\\\[|\\\\\\]|\\\\\\{|\\\\\\}|\\\\\\\\|\\\\\\^|\\\\:|\\\\=|\\\\!|\\\\\\/|\\\\>|\\\\<|\\\\-|\\\\\\(|\\\\\\)|\\\\%|\\\\\\+|\\\\\\?|\\\\;|\\\\'|\\\\~|\\\\\\|";
	
	/**
	 * returns true if a string has one or more special DB2 characters.
	 * @parameter input The string to be checked.
	 * @return True if the given string contains a DB2 special character.
	 */
	public static boolean hasSpecialCharacters(String input){
		Matcher matcher = Pattern.compile(ADD_REGEX).matcher(input);
		return matcher.find();
	}
	
	/**
	 * Adds escape characters to all DB2 special characters in the given string and returns a new string.
	 * @parameter input The string to have escape characters added.
	 * @return A string with escape characters added to all special characters used by DB2.
	 */
	public static String addEscapeCharacters(String input){
		Matcher matcher = Pattern.compile(ADD_REGEX).matcher(input);
		char bs = '\\';
		StringBuilder builder = new StringBuilder(input);
		int inserts = 0;
		while (matcher.find()){
			builder.insert(matcher.start()+inserts, bs);
			inserts++;
		}
		return builder.toString();
	}
	
	/**
	 * Removes escape characters in front of all DB2 special characters in the given string. In effect reverses {@link addEscapeCharacters(String string)} and returns the original string.
	 * @param input String with escapes to be removed.
	 * @return The string with all special character escapes removed.
	 */
	public static String removeEscapeCharacters(String input){
		Matcher matcher = Pattern.compile(REMOVE_REGEX).matcher(input);
		StringBuilder builder = new StringBuilder(input);
		int inserts = 0;
		while (matcher.find()){
			builder.deleteCharAt(matcher.start()-inserts);
			inserts++;
		}
		return builder.toString();
	}
}
