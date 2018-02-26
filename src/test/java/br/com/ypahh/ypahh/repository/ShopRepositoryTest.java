package br.com.ypahh.ypahh.repository;

import static org.junit.Assert.*;

import org.bitbucket.radistao.test.runner.BeforeAfterSpringTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

import br.com.ypahh.ypahh.model.Shop;
import br.com.ypahh.ypahh.util.JsonImporter;

//@IfProfileValue(name = "run.integration.tests", value = "true")
@RunWith(BeforeAfterSpringTestRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ShopRepositoryTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ShopRepository shopRepository;

	private String collectionName = "shop";

	private JsonImporter jsonImporter;

	@Before
	public void setupMethos() {
		jsonImporter = new JsonImporter(mongoTemplate, "src/test/resources/shop/");
	}

	@After
	public void finalizeMethods() {
		mongoTemplate.remove(new Query(), collectionName);
	}

	@Test
	public void testSaveOneShopAndRetriveTheId(){
		Shop shop = new Shop("Casa dos Parafusos", "So tem parafuso", null, null, null);
		
		assertNull(shop.getId());
		
		Shop savedShop = shopRepository.save(shop);
		
		assertNotNull(savedShop.getId().toHexString());
	}
	
	@Test
	public void testFindOneShopByNearesTheUserPointBetweenTwoShops() {
		assertEquals(2, mongoTemplate.getCollection(collectionName).getIndexInfo().size());

		Point point = new Point(-23.182774, -45.885762);
		Distance distance = new Distance(0.100, Metrics.KILOMETERS);

		jsonImporter.execute(collectionName, "test_2_shops.json");

		GeoResults<Shop> shopsNearest = shopRepository.findByLocalizationNear(point, distance);

		assertEquals(1, shopsNearest.getContent().size());
		assertEquals("Casa da Sogra", shopsNearest.getContent().get(0).getContent().getName());
	}

}
