package com.bankntt.msfundtransact.application.exception;

public class AccountNotCreatedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message = "Account couldn't be created";
	
	public AccountNotCreatedException() {

	}

	public String getMessage() {
		return message;
	}}
