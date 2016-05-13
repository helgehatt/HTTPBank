package ibm.resource;

public class InputException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public InputException() {
		super("Invalid input");
	}
	
	public InputException(String message) {
		super(message);
	}
}
