package br.com.ypahh.ypahh.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ypahh.ypahh.datatransferobject.SimpleShopDTO;
import br.com.ypahh.ypahh.mapper.ShopMapper;
import br.com.ypahh.ypahh.model.Product;
import br.com.ypahh.ypahh.model.Shop;
import br.com.ypahh.ypahh.repository.ShopCustomRepository;
import br.com.ypahh.ypahh.repository.ShopRepository;
import br.com.ypahh.ypahh.service.ShopService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/shops")
public class ShopController {

	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ShopCustomRepository shopCustomRepository;
	
	@Autowired
	private ShopService shopService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getAll() {
		List<SimpleShopDTO> shops = ShopMapper.mapperListModelToDTO(shopRepository.findAll());
		return new ResponseEntity<List<SimpleShopDTO>>(shops, HttpStatus.OK);
	}

	@GetMapping(path = "/nearWithProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getAllShopsNearUserHasProduct(
			@RequestParam(name = "productName", required = true) String productName,
			@RequestParam(name = "lat", required = true) Double lat,
			@RequestParam(name = "lon", required = true) Double lon,
			@RequestParam(name = "distanceKm", required = true) Double distanceKm) {
		
		Point pointReferenceUser = new Point(lat, lon);
		List<Shop> nearShops = shopCustomRepository.findByDistanceAndProductContainingWithGrouping(pointReferenceUser, distanceKm, productName);
		//List<SimpleShopDTO> shops = ShopMapper.mapperListModelToDTO(nearShops);
		return new ResponseEntity<List<Shop>>(nearShops, HttpStatus.OK);
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> saveShop(@RequestBody SimpleShopDTO simpleShopDTO) throws Exception {
		Shop shop = ShopMapper.mapper(simpleShopDTO);
		Shop shopSaved = shopService.saveWithFillLocalition(shop);
		return new ResponseEntity<SimpleShopDTO>(ShopMapper.mapper(shopSaved), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path = "/{idShop}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> deleteShop(@PathVariable(name = "idShop") String idShop) throws Exception {
		Shop shop = shopRepository.findByHexId(idShop);
		shopRepository.delete(shop);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PostMapping(path = "/{idShop}/products", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> addProdutcts(
			@RequestBody List<Product> products, 
			@PathVariable(name = "idShop") String idShop) throws Exception {
		shopService.addProducts(idShop, products);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@GetMapping(path = "/{idShop}/products", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getAllProdutcts(@PathVariable(name = "idShop") String idShop) throws Exception {
		Shop shop = shopRepository.findByHexId(idShop);
		return new ResponseEntity<List<Product>>(shop.getProducts(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/{idShop}/products/{idProd}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> getShopWithSpecificProduct(
			@PathVariable(name = "idShop") String idShop, 
			@PathVariable(name = "idProd") String idProd) throws Exception {
		Shop shop = shopCustomRepository.findWithSpecifcProduct(idShop, idProd); 
		return new ResponseEntity<Shop>(shop, HttpStatus.OK);
	}
	
	@PutMapping(path = "/{idShop}/products/{idProd}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> updateProductOneShop(
			@PathVariable(name = "idShop") String idShop, 
			@PathVariable(name = "idProd") String idProd, 
			@RequestBody Product product) throws Exception {
		//TODO: Move to service
		Shop shop = shopRepository.findByHexId(idShop);
		shop.getProducts().forEach(p -> {
			if(p.getCode().equals(idProd)){
				p.setName(product.getName());
				p.setDescription(product.getDescription());
				p.setImages(product.getImages());
			}
		});
		shopRepository.save(shop);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@DeleteMapping(path = "/{idShop}/products/{idProd}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> deleteShop(
			@PathVariable(name = "idShop") String idShop, 
			@PathVariable(name = "idProd") String idProd) throws Exception {
		Shop shop = shopRepository.findByHexId(idShop);
		shop.setProducts(shop.getProducts().stream().filter(p -> !p.getCode().equals(idProd)).collect(Collectors.toList()));
		shopRepository.save(shop);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}
