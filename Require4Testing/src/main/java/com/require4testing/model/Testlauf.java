package com.require4testing.model;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name="testlauf")
public class Testlauf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String beschreibung;
    private String kommentar;
    private String testumgebung;
    
    
    
    
    @ManyToOne
    @JoinColumn(name="tester_id")
    private User tester;
    
    @ManyToOne
    @JoinColumn(name="ersteller_id")
    private User ersteller;
   
  

    // Standard-Konstruktor
    public Testlauf() {}

    // Konstruktor mit Parametern
    public Testlauf(String title, String beschreibung) {
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



   
}