package com.csviewpro.domain.exception;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class FileFormatException extends RuntimeException {

	public FileFormatException() {}

	public FileFormatException(String message) {
		super(message);
	}

	public FileFormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileFormatException(Throwable cause) {
		super(cause);
	}

	public FileFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
