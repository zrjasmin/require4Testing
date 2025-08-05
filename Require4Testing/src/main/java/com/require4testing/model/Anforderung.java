package com.require4testing.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="anforderung")
public class Anforderung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nr;
    private String title;
    private String beschreibung;
    private String prioritaet;
    private String quelle;
    private String notizen;
    
    @Enumerated(EnumType.STRING)
    private AnfKategorie kategorie;
   
    
    @ManyToOne
    @JoinColumn(name="ersteller_id")
    private User ersteller;
    
    @OneToMany(mappedBy="anforderung", cascade = CascadeType.ALL)
    private List<Kriterium> kriterien = new ArrayList<>();

    // Standard-Konstruktor
    public Anforderung() {
    	
    }

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

	

	public String getPrioritaet() {
		return prioritaet;
	}

	public void setPrioritaet(String prioritaet) {
		this.prioritaet = prioritaet;
	}
	
	
	public List<Kriterium> getKriterien() {
	    return kriterien;
	}

	public void setKriterien(List<Kriterium> kriterien) {
	    this.kriterien = kriterien;
	}

	public User getErsteller() {
		return ersteller;
	}

	public void setErsteller(User ersteller) {
		this.ersteller = ersteller;
	}
	
	public void removeKriterium(Kriterium k) {
		kriterien.remove(k);
		k.setAnforderung(null);
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
	
	public  void setKategorie(AnfKategorie kategorie) {
		this.kategorie = kategorie;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}
	

   
}