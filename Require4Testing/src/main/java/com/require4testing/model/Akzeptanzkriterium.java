package com.require4testing.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="akzeptanzkriterium")
public class Akzeptanzkriterium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String beschreibung;
    
    @ManyToOne
    private Anforderung anforderung;

    
    public Akzeptanzkriterium() {
    	
    }
    
	public Akzeptanzkriterium(String beschreibung) {
	    	this.setBeschreibung(beschreibung);	    
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
 
  