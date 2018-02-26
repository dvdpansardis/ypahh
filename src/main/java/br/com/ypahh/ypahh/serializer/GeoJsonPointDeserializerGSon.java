package br.com.ypahh.ypahh.serializer;

import java.lang.reflect.Type;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class GeoJsonPointDeserializerGSon implements JsonDeserializer<GeoJsonPoint> {

	@Override
	public GeoJsonPoint deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject jsonObject = element.getAsJsonObject();
		JsonArray coords = jsonObject.get("coordinates").getAsJsonArray();
		return new GeoJsonPoint (coords.get(0).getAsDouble(), coords.get(1).getAsDouble());
	}

}
