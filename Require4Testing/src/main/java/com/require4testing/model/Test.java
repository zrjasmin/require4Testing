package com.require4testing.model;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Table(name="test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String beschreibung;
    
    @ManyToOne
    @JoinColumn(name="ersteller_id")
    private User ersteller;
    
    @ManyToOne
    @JoinColumn(name="tester_id")
    private User tester;
    
   
  

    // Standard-Konstruktor
    public Test() {}

    // Konstruktor mit Parametern
    public Test(String title, String beschreibung) {
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
	
	public User getErsteller() {
		return ersteller;
	}
	
	public void setErsteller(User ersteller) {
		this.ersteller = ersteller;
		
	}
	
	public User getTester() {
		return tester;
	}
	
	public void setTester(User tester) {
		this.tester = tester;
		
	}



   
}