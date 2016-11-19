package com.csviewpro.domain.conversion;

import javafx.util.converter.DoubleStringConverter;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class DoubleConverter implements TypeConverter<Double>{

	@Override
	public Double convert(String s) {
		if(s == null || s.trim().isEmpty())
			return 0d;

		try {
			return new DoubleStringConverter().fromString(s);
		}catch (Exception e){
			return 0d;
		}
	}
}
