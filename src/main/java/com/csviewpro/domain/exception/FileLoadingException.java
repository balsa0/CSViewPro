package com.csviewpro.domain.exception;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class FileLoadingException extends RuntimeException {

	public FileLoadingException() {
	}

	public FileLoadingException(String message) {
		super(message);
	}

	public FileLoadingException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileLoadingException(Throwable cause) {
		super(cause);
	}

	public FileLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
