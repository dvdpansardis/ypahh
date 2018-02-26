package br.com.ypahh.ypahh.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

public class JsonImporter {
	
	private MongoTemplate mongoTemplate;
	private String basePathResource = "";
	
	public JsonImporter(MongoTemplate mongoTemplate, String basePathResource) {
		this.mongoTemplate = mongoTemplate;
		this.basePathResource = basePathResource;
	}

	public void execute(String collection, String file) {
		try {
			for (Object line : FileUtils.readLines(new File(basePathResource + file), "utf8")) {
				mongoTemplate.save(line, collection);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not import file: " + file, e);
		}
	}
	
}
