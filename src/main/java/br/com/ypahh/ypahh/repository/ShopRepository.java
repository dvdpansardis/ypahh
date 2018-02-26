package br.com.ypahh.ypahh.repository;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import br.com.ypahh.ypahh.model.Shop;

public interface ShopRepository extends MongoRepository<Shop, Long> {

	GeoResults<Shop> findByLocalizationNear(Point point, Distance distance);

	@Query("{'_id': ?0}")
	Shop findByHexId(String id);
	
}
