package com.csviewpro.domain.conversion;

import javafx.util.converter.NumberStringConverter;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class LongConverter implements TypeConverter<Long>{

	@Override
	public Long convert(String s) {
		if(s == null || s.trim().isEmpty())
			return 0L;

		try {
			return new NumberStringConverter().fromString(s).longValue();
		}catch (ClassCastException e){
			return 0L;
		}
	}
}
