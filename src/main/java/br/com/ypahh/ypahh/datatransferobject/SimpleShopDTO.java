package br.com.ypahh.ypahh.datatransferobject;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.ypahh.ypahh.model.Contact;
import br.com.ypahh.ypahh.model.Localization;

public class SimpleShopDTO {

	@JsonProperty(required = false)
	private String id;

	private String name;

	private String description;

	private Localization localization;

	private Contact contact;

	public SimpleShopDTO() {
	}

	public SimpleShopDTO(String name, String description, Contact contact, Localization localization) {
		this.name = name;
		this.description = description;
		this.contact = contact;
		this.localization = localization;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

}
