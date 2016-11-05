package com.csviewpro.config;

import com.csviewpro.domain.conversion.DateConverter;
import com.csviewpro.domain.conversion.DoubleConverter;
import com.csviewpro.domain.conversion.LongConverter;
import com.csviewpro.domain.conversion.StringConverter;
import com.csviewpro.service.TypeConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by Balsa on 2016. 10. 15..
 */
@Configuration
public class ConverterConfiguration {

	@Autowired
	private TypeConverterService typeConverterService;

	@PostConstruct
	private void registerConverters(){
		typeConverterService.registerConverter(String.class, new StringConverter());
		typeConverterService.registerConverter(Long.class, new LongConverter());
		typeConverterService.registerConverter(Double.class, new DoubleConverter());
		typeConverterService.registerConverter(Date.class, new DateConverter());
	}

}