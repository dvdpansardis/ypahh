package br.com.ypahh.ypahh.service;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ypahh.ypahh.model.Localization;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GeolocalizationServiceTest {

	@Autowired
	private GeolocalizationService geolocatiozationService;
	
	@Test
	public void testFillWithCoordinates() throws Exception{
		Localization localization = new Localization();
		localization.setAddress("Av. Dr. Eduardo Cury, 200 - Jardim das Colinas São José dos Campos - SP 12242-001");
		
		geolocatiozationService.fillLocation(localization);
		
		assertEquals(-23.2023512, localization.getLocation().getCoordinates().get(0), 0.0001);
		assertEquals(-45.9084535, localization.getLocation().getCoordinates().get(1), 0.0001);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testFillWithoutAddressExpectedException() throws Exception{
		Localization localization = new Localization();
		localization.setAddress(null);
		
		geolocatiozationService.fillLocation(localization);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testFillWithInvalidAddressExpectedException() throws Exception{
		Localization localization = new Localization();
		localization.setAddress("xyzwk");
		
		geolocatiozationService.fillLocation(localization);
	}
}
