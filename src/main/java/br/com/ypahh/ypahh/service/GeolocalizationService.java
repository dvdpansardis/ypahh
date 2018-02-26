package br.com.ypahh.ypahh.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

import br.com.ypahh.ypahh.model.Localization;

@Service
public class GeolocalizationService {
	
	@Value("${google.api.key}")
	private String key;

	public void fillLocation(Localization localization) throws Exception{
		if(localization.getAddress() == null){
			throw new IllegalArgumentException("No has address.");
		}
		
		if(localization.getLocation() != null) return; //TODO: make test
			
		GeoApiContext geoApiContext = new GeoApiContext().setApiKey(key);
		
		GeocodingApiRequest request = GeocodingApi.newRequest(geoApiContext).address(localization.getAddress());
		
		GeocodingResult[] results = request.await();
		
		if(results.length == 0){
			throw new NoSuchElementException("Not does found any address.");
		}
		
		GeocodingResult result = results[0];
		
		Geometry geometry = result.geometry;
		
		LatLng location = geometry.location;

		localization.setLocation(new GeoJsonPoint(location.lat, location.lng));
	}
	
}
