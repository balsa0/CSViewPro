package com.csviewpro.domain.conversion;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class LongConverter implements TypeConverter<Long>{

	@Override
	public Long convert(String s) {
		if(s == null)
			return 0L;

		return Long.valueOf(s);
	}
}
