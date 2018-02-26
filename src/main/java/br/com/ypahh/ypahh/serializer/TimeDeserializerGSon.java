package br.com.ypahh.ypahh.serializer;

import java.lang.reflect.Type;
import java.time.LocalTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class TimeDeserializerGSon implements JsonDeserializer<LocalTime> {

	@Override
	public LocalTime deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		String time = element.getAsString();
		
		return LocalTime.parse(time);
	}

}
