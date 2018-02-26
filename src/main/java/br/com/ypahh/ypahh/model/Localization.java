package br.com.ypahh.ypahh.model;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import br.com.ypahh.ypahh.serializer.GeoJsonPointSerializerJackson;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Localization {

	private String address;

	@JsonProperty(required = false)
	@JsonSerialize(using = GeoJsonPointSerializerJackson.class)
	@GeoSpatialIndexed(name = "location", type = GeoSpatialIndexType.GEO_2DSPHERE) 
	private GeoJsonPoint location;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GeoJsonPoint getLocation() {
		return location;
	}

	public void setLocation(GeoJsonPoint location) {
		this.location = location;
	}

}
