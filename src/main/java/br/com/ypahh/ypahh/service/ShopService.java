package br.com.ypahh.ypahh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ypahh.ypahh.model.Product;
import br.com.ypahh.ypahh.model.Shop;
import br.com.ypahh.ypahh.repository.ShopRepository;

@Service
public class ShopService {

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private GeolocalizationService geoLocalizationService;
	
	public Shop saveWithFillLocalition(Shop shop) throws Exception{
		geoLocalizationService.fillLocation(shop.getLocalization());
		
		Shop saved = shopRepository.save(shop);
		
		return saved;
	}

	public void addProducts(String idHexShop, List<Product> products) {
		Shop shopFound = shopRepository.findByHexId(idHexShop);
		if(shopFound != null){
			shopFound.addAll(products);
		}
		shopRepository.save(shopFound);
	}

}
