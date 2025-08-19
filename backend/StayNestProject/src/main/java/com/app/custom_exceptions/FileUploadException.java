package com.app.custom_exceptions;

public class FileUploadException extends RuntimeException{
	public FileUploadException(String message) {
		super(message);
	}
}
