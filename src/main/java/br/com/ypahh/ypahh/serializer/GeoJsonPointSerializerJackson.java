package br.com.ypahh.ypahh.serializer;

import java.io.IOException;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GeoJsonPointSerializerJackson extends JsonSerializer<GeoJsonPoint> {
	@Override
	public void serialize(GeoJsonPoint value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeStringField("type", value.getType());
		gen.writeArrayFieldStart("coordinates");
		gen.writeObject(value.getCoordinates().get(0));
		gen.writeObject(value.getCoordinates().get(1));
		gen.writeEndArray();
		gen.writeEndObject();
	}

}
