package com.csviewpro.domain.conversion;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class StringConverter implements TypeConverter<String>{

	@Override
	public String convert(String s) {
		return s;
	}
}
