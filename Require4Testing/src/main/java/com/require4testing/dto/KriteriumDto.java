package com.require4testing.dto;


public class KriteriumDto {
	
	private Long id;
	private String beschreibung;
	
	
	public KriteriumDto() {}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
}