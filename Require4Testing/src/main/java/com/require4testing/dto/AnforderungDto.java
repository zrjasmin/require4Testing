package com.require4testing.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.require4testing.model.AnfKategorie;
import com.require4testing.model.Prioritaet;



public class AnforderungDto {
	
	private Long id;
    private String title;
    private String beschreibung;
    private Prioritaet prioritaet;
    private String quelle;
    private String notizen;
    private AnfKategorie kategorie;
    
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

	public Prioritaet getPrioritaet() {
		return prioritaet;
	}

	public void setPrioritaet(Prioritaet prioritaet) {
		this.prioritaet = prioritaet;
	}


	public List<KriteriumDto> getKriterien() {
		return kriterien;
	}

	public void setKriterien(List<KriteriumDto> kriterien) {
		this.kriterien = kriterien;
	}

	public String getQuelle() {
		return quelle;
	}

	public void setQuelle(String quelle) {
		this.quelle = quelle;
	}

	public String getNotizen() {
		return notizen;
	}

	public void setNotizen(String notizen) {
		this.notizen = notizen;
	}

	public AnfKategorie getKategorie() {
		return kategorie;
	}

	public void setKategorie(AnfKategorie kategorie) {
		this.kategorie = kategorie;
	}
}