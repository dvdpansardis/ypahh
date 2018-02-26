package br.com.ypahh.ypahh.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ypahh.ypahh.model.Localization;
import br.com.ypahh.ypahh.model.Product;
import br.com.ypahh.ypahh.model.Shop;
import br.com.ypahh.ypahh.repository.ShopRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ShopServiceTest {

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private ShopRepository shopRepository;

	@Test
	public void testAddProductsToShop(){
		Shop shop = new Shop("Casa dos Parafusos", "So tem parafuso", null, null, null);
		
		Shop shopSaved = shopRepository.save(shop);
		
		Product prod1 = new Product("123", "10 polegadas", "Parafuso de 10 polegadas");
		Product prod2 = new Product("456", "15 polegadas", "Parafuso de 15 polegadas");
		
		List<Product> products = Arrays.asList(prod1, prod2);
		
		shopService.addProducts(shopSaved.getId().toHexString(), products);
		
		Shop shopRetrived = shopRepository.findByHexId(shop.getId().toHexString());
		
		assertEquals(2, shopRetrived.getProducts().size());
		assertEquals("123", shopRetrived.getProducts().get(0).getCode());
		assertEquals("456", shopRetrived.getProducts().get(1).getCode());
	}
	
	@Test
	public void testSaveANewShopWithouLocalizationAndFillThat() throws Exception{
		Localization localization = new Localization();
		localization.setAddress("R. Sete de Setembro, 202 - Centro, São José dos Campos - SP, 12210-260");

		Shop shop = new Shop("Casa dos Parafusos", "So tem parafuso", null, localization, null);
		
		Shop shopSaved = shopService.saveWithFillLocalition(shop);
		
		assertNotNull(shopSaved.getId().toHexString());
		assertNotNull(shopSaved.getLocalization().getLocation());
		assertNotNull(shopSaved.getLocalization().getLocation().getX());
		assertNotNull(shopSaved.getLocalization().getLocation().getY());
	}
	
	@Test
	public void testSaveANewShopWithLocalizationAndNotExecutingGeolocation() throws Exception{
		Localization localization = new Localization();
		localization.setAddress("R. Sete de Setembro, 202 - Centro, São José dos Campos - SP, 12210-260");
		localization.setLocation(new GeoJsonPoint(1.0, 2.0));

		Shop shop = new Shop("Casa dos Parafusos", "So tem parafuso", null, localization, null);
		
		Shop shopSaved = shopService.saveWithFillLocalition(shop);
		
		assertNotNull(shopSaved.getId().toHexString());
		assertNotNull(shopSaved.getLocalization().getLocation());
		assertEquals(1.0, shopSaved.getLocalization().getLocation().getX(), 0.00001);
		assertEquals(2.0, shopSaved.getLocalization().getLocation().getY(), 0.00001);
	}
}
