package com.api.annotations;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SerializeJsonLongDateFormat extends JsonSerializer<Date>{

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider provider)throws IOException, JsonProcessingException {
		
		String formattedDate = dateFormat.format(date);
		jsonGenerator.writeString(formattedDate);
	}
}
