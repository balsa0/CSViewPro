package com.csviewpro.domain.conversion;

/**
 * This interface is an unified interface for converting string different types (parsing them).
 */
public interface TypeConverter<T>{

	T convert(String s);

}
