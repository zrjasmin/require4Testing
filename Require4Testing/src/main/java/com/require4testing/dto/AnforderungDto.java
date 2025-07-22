package com.require4testing.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class AnforderungDto {
	
	private Long id;
    private String title;
    private String beschreibung;
    private String priotiät;
    
    private List<KriteriumDto> kriterien = new ArrayList<>();
    
    public AnforderungDto() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getPriotiät() {
		return priotiät;
	}

	public void setPriotiät(String priotiät) {
		this.priotiät = priotiät;
	}


	public List<KriteriumDto> getKriterien() {
		return kriterien;
	}

	public void setKriterien(List<KriteriumDto> kriterien) {
		this.kriterien = kriterien;
	}
}