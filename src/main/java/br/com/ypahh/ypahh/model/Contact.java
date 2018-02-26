package br.com.ypahh.ypahh.model;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Contact {

	private String telephone;

	private String whatsup;

	private String email;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime openTime;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime closeTime;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getWhatsup() {
		return whatsup;
	}

	public void setWhatsup(String whatsup) {
		this.whatsup = whatsup;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

}
