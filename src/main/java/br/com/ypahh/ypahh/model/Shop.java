package br.com.ypahh.ypahh.model;

import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "shop")
public class Shop {

	@Id
	private ObjectId id;

	private String name;

	private String description;

	private Localization localization;

	private Contact contact;

	@JsonProperty(required = false)
	private List<Product> products;

	public Shop() {
	}

	public Shop(String name, String description, Contact contact, Localization localization, List<Product> products) {
		this.name = name;
		this.description = description;
		this.contact = contact;
		this.localization = localization;
		this.products = products;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Localization getLocalization() {
		return localization;
	}

	public void setLocalization(Localization localization) {
		this.localization = localization;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public void addAll(List<Product> productsToAdd) {
		if(products == null){
			products = new LinkedList<>();
		}
		products.addAll(productsToAdd);
	}

}
