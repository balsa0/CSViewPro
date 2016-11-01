package com.csviewpro.domain.conversion;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.Date;
import java.util.List;

/**
 * Created by Balsa on 2016. 11. 01..
 */
public class DateConverter implements TypeConverter<Date>{

	@Override
	public Date convert(String s) {
		if(s == null)
			return new Date();

		// parse to date group list
		List<DateGroup> dateGroupList = new Parser().parse(s);

		Date result = new Date();

		for(DateGroup dateGroup : dateGroupList){
			// skip on recurring dates
			if(dateGroup.isRecurring())
				continue;

			// check dates and select the oldest
			for(Date date : dateGroup.getDates()){
				if(date.before(result))
					result = date;
			}
		}

		return result;
	}
}
