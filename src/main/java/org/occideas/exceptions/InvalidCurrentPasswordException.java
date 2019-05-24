package org.occideas.exceptions;

public class InvalidCurrentPasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public InvalidCurrentPasswordException(String message) {
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
