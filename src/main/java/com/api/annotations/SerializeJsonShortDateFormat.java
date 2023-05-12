package com.api.annotations;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

@Component
public class SerializeJsonShortDateFormat extends JsonSerializer<Date>{

	private static Logger LOGGER = LoggerFactory.getLogger(SerializeJsonShortDateFormat.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider provider)throws IOException, JsonProcessingException {
		
		LOGGER.info("Inside into short serialize method");
		
		String formattedDate = dateFormat.format(date);
		jsonGenerator.writeString(formattedDate);
	}

}
