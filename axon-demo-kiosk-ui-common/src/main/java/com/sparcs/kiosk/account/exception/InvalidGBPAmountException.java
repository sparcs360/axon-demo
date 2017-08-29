package com.sparcs.kiosk.account.exception;

public class InvalidGBPAmountException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public InvalidGBPAmountException() {
		super("Invalid GBP amount specified - must be a positive number");
	}
}
