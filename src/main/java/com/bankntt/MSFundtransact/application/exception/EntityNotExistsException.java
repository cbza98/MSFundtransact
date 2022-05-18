package com.bankntt.MSFundtransact.application.exception;

public class EntityNotExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private String message = "The Business Partner doesn't exists";

	public EntityNotExistsException() {

	}

	public String getMessage() {
		return message;
	}
}