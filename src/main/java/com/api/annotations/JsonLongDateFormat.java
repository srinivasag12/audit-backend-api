package com.api.annotations;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonLongDateFormat extends JsonDeserializer<Date>{

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
			throws IOException {
		
		if(paramJsonParser==null || paramJsonParser.getText().trim().equals("") ){	return null;}
		
		String stringDate = paramJsonParser.getText().trim();
		
		try {
			
			java.util.Date date = dateFormat.parse(stringDate);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.MILLISECOND, 0);
			
			return new java.sql.Timestamp(date.getTime());
			
		} catch (ParseException e) {
          
		}
		
		java.util.Date date2 = paramDeserializationContext.parseDate(stringDate);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date2);
		cal.set(Calendar.MILLISECOND, 0);
		
		return new java.sql.Timestamp(date2.getTime());
		
	}

}
