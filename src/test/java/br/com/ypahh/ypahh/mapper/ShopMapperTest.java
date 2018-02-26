package br.com.ypahh.ypahh.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bson.types.ObjectId;
import org.junit.Test;

import br.com.ypahh.ypahh.datatransferobject.SimpleShopDTO;
import br.com.ypahh.ypahh.model.Localization;
import br.com.ypahh.ypahh.model.Shop;

public class ShopMapperTest {

	@Test
	public void testMappingShopToSimpleShopDTO() {
		Localization localization = new Localization();
		localization.setAddress("R. Sete de Setembro, 202 - Centro, São José dos Campos - SP, 12210-260");

		Shop shop = new Shop("Casa dos Parafusos", "So tem parafuso", null, localization, null);
		shop.setId(new ObjectId("5a8d640e80ae9128643c99ff"));

		SimpleShopDTO simpleShopDTO = ShopMapper.mapper(shop);

		assertNotNull(simpleShopDTO.getId());
		assertEquals(shop.getName(), simpleShopDTO.getName());
		assertEquals(shop.getDescription(), simpleShopDTO.getDescription());
		assertEquals(shop.getLocalization().getAddress(), simpleShopDTO.getLocalization().getAddress());
	}
	
	@Test
	public void testMappingSimpleShopDTOToShop() {
		Localization localization = new Localization();
		localization.setAddress("R. Sete de Setembro, 202 - Centro, São José dos Campos - SP, 12210-260");

		SimpleShopDTO simpleShopDTO = new SimpleShopDTO("Casa dos Parafusos", "So tem parafuso", null, localization);
		simpleShopDTO.setId("5a8d640e80ae9128643c99ff");
		
		Shop shop = ShopMapper.mapper(simpleShopDTO);

		assertNotNull(shop.getId().toHexString());
		assertEquals(simpleShopDTO.getName(), shop.getName());
		assertEquals(simpleShopDTO.getDescription(), shop.getDescription());
		assertEquals(simpleShopDTO.getLocalization().getAddress(), shop.getLocalization().getAddress());
	}

}
