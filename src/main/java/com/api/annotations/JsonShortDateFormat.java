package com.api.annotations;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JsonShortDateFormat extends JsonDeserializer<Date> {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
			throws IOException {
		
		if(paramJsonParser==null || paramJsonParser.getText().trim().equals("") ){	return null;}
		
		String stringDate = paramJsonParser.getText().trim();
		
		try {
			java.util.Date date = dateFormat.parse(stringDate);
			java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
			
			return sqlStartDate;
		} catch (ParseException e) {
           
		}
		return paramDeserializationContext.parseDate(stringDate);
	}
}
