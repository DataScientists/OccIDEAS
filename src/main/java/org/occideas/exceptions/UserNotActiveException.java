package org.occideas.exceptions;

public class UserNotActiveException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	
	public UserNotActiveException(String message) {
		this.setMessage(message);
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
