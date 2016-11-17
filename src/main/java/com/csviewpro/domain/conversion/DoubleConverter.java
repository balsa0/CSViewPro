package com.csviewpro.domain.conversion;

import javafx.util.converter.NumberStringConverter;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class DoubleConverter implements TypeConverter<Double>{

	@Override
	public Double convert(String s) {
		if(s == null || s.trim().isEmpty())
			return 0d;

		try {
			return new NumberStringConverter().fromString(s).doubleValue();
		}catch (ClassCastException e){
			return 0d;
		}
	}
}
