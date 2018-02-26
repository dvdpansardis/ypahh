package br.com.ypahh.ypahh.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.ypahh.ypahh.datatransferobject.SimpleShopDTO;
import br.com.ypahh.ypahh.model.Localization;
import br.com.ypahh.ypahh.model.Product;
import br.com.ypahh.ypahh.model.Shop;
import br.com.ypahh.ypahh.repository.ShopRepository;
import br.com.ypahh.ypahh.serializer.GeoJsonPointDeserializerGSon;
import br.com.ypahh.ypahh.serializer.TimeDeserializerGSon;
import br.com.ypahh.ypahh.util.JsonImporter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ShopControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ShopRepository shopRepository;
	
	private TestRestTemplate restTemplate = new TestRestTemplate();

	private String collectionName = "shop";

	private JsonImporter jsonImporter;
	
	private Type listType = new TypeToken<List<SimpleShopDTO>>(){}.getType();
	
	@Before
	public void setupMethos() {
		jsonImporter = new JsonImporter(mongoTemplate, "src/test/resources/shop/");
	}
	
	@After
	public void finalizeMethods() {
		mongoTemplate.remove(new Query(), collectionName);
	}
	
	@Test
	public void testFindNearShopsWithProduct(){
		jsonImporter.execute(collectionName, "test_3_shops.json");

		String uri = "shops/nearWithProduct?productName=Body&lat=-23.182774&lon=-45.885762&distanceKm=0.1";
		
		String result = restTemplate.getForObject(createURLWithPort(uri), String.class);
		
		List<SimpleShopDTO> shopResult = getGson().fromJson(result, listType);
		
		assertEquals(2, shopResult.size());
	}
	
	@Test
	public void testAddProductsToShop(){
		jsonImporter.execute(collectionName, "test_1_simple_shop.json");
		
		Product prod1 = new Product("123", "10 polegadas", "Parafuso de 10 polegadas");
		Product prod2 = new Product("456", "15 polegadas", "Parafuso de 15 polegadas");
		
		List<Product> products = Arrays.asList(prod1, prod2);
		
		String idShop = "5a8d640e80ae9128643c99fe";
		
		ResponseEntity<String> result = restTemplate.postForEntity(createURLWithPort("shops/" + idShop + "/products"), products, String.class);
		
		Shop shopFound = shopRepository.findByHexId(idShop);
		
		assertEquals(201, result.getStatusCodeValue());
		assertEquals(3, shopFound.getProducts().size());
	}
	
	@Test
	public void testSaveTheShopWithoutCoordinatesAndServerRetrieveCoordinates() throws JSONException {
		Localization localization = new Localization();
		localization.setAddress("R. Sete de Setembro, 202 - Centro, São José dos Campos - SP, 12210-260");

		Shop shop = new Shop("Casa dos Parafusos", "So tem parafuso", null, localization, null);

		String result = restTemplate.postForObject(createURLWithPort("shops"), shop, String.class);

		SimpleShopDTO shopResult = new Gson().fromJson(result, SimpleShopDTO.class);
		
		assertNotNull(shopResult.getId());
		assertEquals("Casa dos Parafusos", shopResult.getName());
		assertNotNull(shopResult.getLocalization().getLocation());
		assertNotNull(shopResult.getLocalization().getLocation().getX());
		assertNotNull(shopResult.getLocalization().getLocation().getY());
	}

	private Gson getGson(){
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(LocalTime.class, new TimeDeserializerGSon());
		gsonBuilder.registerTypeAdapter(GeoJsonPoint.class, new GeoJsonPointDeserializerGSon());
		return gsonBuilder.create();
	}
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + "/" + uri;
	}
}
