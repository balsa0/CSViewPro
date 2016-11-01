package com.csviewpro.domain.conversion;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class DoubleConverter implements TypeConverter<Double>{

	@Override
	public Double convert(String s) {
		if(s == null)
			return 0d;
		
		return Double.valueOf(s);
	}
}
