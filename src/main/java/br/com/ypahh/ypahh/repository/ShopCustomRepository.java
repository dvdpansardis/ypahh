package br.com.ypahh.ypahh.repository;

import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GeoNearOperation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;

import br.com.ypahh.ypahh.model.Shop;

@Repository
public class ShopCustomRepository {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	private static final String INSENSITIVE_CASE = "(?i)\\w*";

	public List<Shop> findByDistanceAndProductContaining(Point pointReference, Double distanceKm, String productName) {
		return mongoTemplate.find(
				query(Criteria.where("localization.location").nearSphere(pointReference)
						.maxDistance(new Distance(distanceKm, Metrics.KILOMETERS).getNormalizedValue()).and("products")
						.elemMatch(Criteria.where("name").regex("Body"))).with(new Sort(Sort.Direction.ASC, "name")),
				Shop.class);
	}

	public List<Shop> findByDistanceAndProductContainingWithGrouping(Point pointReference, Double distanceKm, String productName) {
		final NearQuery nearQuery = NearQuery.near(pointReference, Metrics.KILOMETERS);
		nearQuery.maxDistance(distanceKm);
		nearQuery.spherical(true);
		GeoNearOperation geoNearShops = Aggregation.geoNear(nearQuery, "distance");
		
		SortOperation sortOperation = Aggregation.sort(Direction.ASC, "name");
		
		MatchOperation matchProduct = Aggregation.match(new Criteria("products.name").regex(INSENSITIVE_CASE + productName));
		
		UnwindOperation unwindProducts = Aggregation.unwind("$products");
		
		GroupOperation groupOperation = Aggregation.group("id")
				.first("name").as("name")
				.first("description").as("description")
				.first("contact").as("contact")
				.first("localization").as("localization")
				.push(new BasicDBObject("code", "$products.code")
						.append("name", "$products.name")
						.append("images", "$products.images")
						.append("description", "$products.description")).as("products");
				
		
		final Aggregation agg = Aggregation.newAggregation(geoNearShops, sortOperation, matchProduct, unwindProducts, matchProduct, groupOperation);
		
		AggregationResults<Shop> groupResults = mongoTemplate.aggregate(agg, Shop.class, Shop.class);
		List<Shop> result = groupResults.getMappedResults();
		
		return result;
	}
	
	//TODO: Create Test
	public Shop findWithSpecifcProduct(String idHexShop, String productCode) {
		MatchOperation matchShop = Aggregation.match(new Criteria("_id").is(new ObjectId(idHexShop)));

		UnwindOperation unwindProducts = Aggregation.unwind("$products");

		MatchOperation matchProduct = Aggregation.match(new Criteria("products.code").regex(INSENSITIVE_CASE + productCode));
				
		GroupOperation groupOperation = Aggregation.group("id")
				.first("name").as("name")
				.first("description").as("description")
				.first("contact").as("contact")
				.first("localization").as("localization")
				.push(new BasicDBObject("code", "$products.code")
						.append("name", "$products.name")
						.append("images", "$products.images")
						.append("description", "$products.description")).as("products");
		
		final Aggregation agg = Aggregation.newAggregation(matchShop, unwindProducts, matchProduct, groupOperation);
		
		AggregationResults<Shop> groupResults = mongoTemplate.aggregate(agg, Shop.class, Shop.class);
		Shop result = groupResults.getUniqueMappedResult();
		
		return result;
	}
}
