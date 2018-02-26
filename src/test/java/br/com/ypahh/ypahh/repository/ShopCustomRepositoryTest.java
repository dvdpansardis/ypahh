package br.com.ypahh.ypahh.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.bitbucket.radistao.test.runner.BeforeAfterSpringTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class ShopCustomRepositoryTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private ShopCustomRepository shopCustomRepository;

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
	public void testFindOneShopByNearesTheUserPointBetweenTwoShopsAndHasTheProductWithGrouping() {
		assertEquals(2, mongoTemplate.getCollection(collectionName).getIndexInfo().size());

		Point point = new Point(-23.182774, -45.885762);

		jsonImporter.execute(collectionName, "test_3_shops.json");

		List<Shop> shopsNearest = shopCustomRepository.findByDistanceAndProductContainingWithGrouping(point, 0.1, "Body");
		
		assertEquals(2, shopsNearest.size());
		assertEquals("123", shopsNearest.get(0).getProducts().get(0).getCode());
		assertEquals("666", shopsNearest.get(1).getProducts().get(0).getCode());
	}
	
	@Test
	public void testFindOneShopByNearesTheUserPointBetweenTwoShopsAndHasTheProduct() {
		assertEquals(2, mongoTemplate.getCollection(collectionName).getIndexInfo().size());

		Point point = new Point(-23.182774, -45.885762);

		jsonImporter.execute(collectionName, "test_3_shops.json");

		List<Shop> shopsNearest = shopCustomRepository.findByDistanceAndProductContaining(point, 0.1, "Body");
		
		assertEquals(2, shopsNearest.size());
		assertEquals("123", shopsNearest.get(0).getProducts().get(0).getCode());
		assertEquals("666", shopsNearest.get(1).getProducts().get(0).getCode());
	}

}
