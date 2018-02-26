package br.com.ypahh.ypahh.config;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "br.com.ypahh.ypahh.repository")
@Configuration
public class ConfigMongoDB {

	@Autowired
	MongoDbFactory mongoDbFactory;

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {

		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, getDefaultMongoConverter());

		@SuppressWarnings("rawtypes")
		List<Converter> converters = new LinkedList<Converter>();

		converters.add(new LocalTimeToStringConverter());
		converters.add(new StringToLocalTimeConverter());

		CustomConversions cc = new CustomConversions(converters);

		((MappingMongoConverter) mongoTemplate.getConverter()).setCustomConversions(cc);
		((MappingMongoConverter) mongoTemplate.getConverter()).afterPropertiesSet();
		
		return mongoTemplate;

	}

	@Bean
	public MappingMongoConverter getDefaultMongoConverter() throws Exception {

		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory),
				new MongoMappingContext());

		return converter;
	}
	
	class LocalTimeToStringConverter implements Converter<LocalTime, String> {
	    @Override
	    public String convert(LocalTime localTime) {
	        return localTime.toString();
	    }
	}
	
	class StringToLocalTimeConverter implements Converter<String, LocalTime> {
	    @Override
	    public LocalTime convert(String s) {
	        return LocalTime.parse(s);
	    }
	}

}
