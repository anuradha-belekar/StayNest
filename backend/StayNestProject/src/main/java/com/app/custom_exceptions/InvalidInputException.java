package com.app.custom_exceptions;

public class InvalidInputException extends RuntimeException{
	public InvalidInputException(String message) {
		super(message);
	}
}
