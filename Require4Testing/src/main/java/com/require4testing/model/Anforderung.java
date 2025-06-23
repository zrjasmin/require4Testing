package com.require4testing.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@Table(name="anforderung")
public class Anforderung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String beschreibung;
    private String priotiät;
    private Date erstelltAm;
    @OneToMany(mappedBy = "anforderung", cascade = CascadeType.ALL)
    private List<Akzeptanzkriterium> akzeptanzkriterien;

    // Standard-Konstruktor
    public Anforderung() {}

    // Konstruktor mit Parametern
    public Anforderung(String title, String beschreibung) {
        this.setTitle(title);
        this.setBeschreibung(beschreibung);
    }

    // Getter und Setter
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

	public Date getErstelltAm() {
		return erstelltAm;
	}

	public void setErstelltAm(Date erstelltAm) {
		this.erstelltAm = erstelltAm;
	}

	public String getPriotiät() {
		return priotiät;
	}

	public void setPriotiät(String priotiät) {
		this.priotiät = priotiät;
	}
	
	
	public List<Akzeptanzkriterium> getAkzeptanzkriterien() {
	    return akzeptanzkriterien;
	}

	public void setAkzeptanzkriterien(List<Akzeptanzkriterium> akzeptanzkriterien) {
	    this.akzeptanzkriterien = akzeptanzkriterien;
	}

   
}