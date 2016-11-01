package com.csviewpro.domain.conversion;

/**
 * Created by Balsa on 2016. 10. 31..
 */
public class TypeConversionException extends RuntimeException {

	public TypeConversionException() {}

	public TypeConversionException(String message) {
		super(message);
	}

	public TypeConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeConversionException(Throwable cause) {
		super(cause);
	}

	public TypeConversionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
