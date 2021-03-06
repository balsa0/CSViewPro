package com.csviewpro.service.parser;

import com.csviewpro.domain.conversion.TypeConversionException;
import com.csviewpro.domain.conversion.TypeConverter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Balsa on 2016. 11. 01..
 */
@Service
public class TypeConverterService {

	// the registered converters
	private Map<Class, TypeConverter> registeredConverters = new HashMap<>();

	public void registerConverter(Class clazz, TypeConverter typeConverter){
		registeredConverters.put(clazz, typeConverter);
	}

	/**
	 * This method converts a string to a given type.
	 * @param type the destination type of the conversion.
	 * @param s the source string of the conversion. This will be converted.
	 * @return the converted object.
	 */
	public Object convertTo(Class type, String s){
		// get the required converter
		TypeConverter converter = registeredConverters.get(type);

		// null check
		if(converter == null)
			throw new TypeConversionException("Could not find converter for " + type.getName() + " type. Have you registered it?.");

		// do the actual parser
		return converter.convert(s);
	}
}
