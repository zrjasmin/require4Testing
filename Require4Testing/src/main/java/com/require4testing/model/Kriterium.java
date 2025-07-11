package com.require4testing.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Table(name="kriterium")
public class Kriterium {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String beschreibung;
	
	@ManyToOne
	@JoinColumn(name = "anf_id")
	private Anforderung anforderung;
	
	public Kriterium() {}
	
	public Kriterium(String beschreibung) {
		
	}
	
	public Long getId() {
		return id;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
	public Anforderung getAnforderung() {
		return anforderung;
	}

	public void setAnforderung(Anforderung anf) {
		this.anforderung = anf;
	}
	
	
	
	
	
}