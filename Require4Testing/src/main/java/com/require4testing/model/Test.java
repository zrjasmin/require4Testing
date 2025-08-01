package com.require4testing.model;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name="test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nr;
    private String title;
    private String beschreibung;
    private String erwartetesErgebnis;
    private String testdaten;
    private String notizen;
    
    
    @ManyToOne
    @JoinColumn(name="ersteller_id")
    private User ersteller;
   
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "anf_id", referencedColumnName = "id")
    private Anforderung anforderung;
   
    @OneToMany(mappedBy="test", cascade = CascadeType.ALL)
    private List<Testschritt> testschritte;
  
    @ManyToMany(mappedBy = "tests")
    private Set<Testlauf> testlaeufe = new HashSet<>();
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
	
	
	public Anforderung getAnforderung() {
		return anforderung;
	}

	public void setAnforderung(Anforderung anf) {
		this.anforderung = anf;
	}

	public String getErwartetesErgebnis() {
		return erwartetesErgebnis;
	}

	public void setErwartetesErgebnis(String erwartetesErgebnis) {
		this.erwartetesErgebnis = erwartetesErgebnis;
	}
	
	public List<Testschritt> getTestschritte() {
		return testschritte;
	}

	public void setTestschritte(List<Testschritt> schritte) {
		this.testschritte = schritte;
	}
	
	public void removeSchritt(Testschritt schritt) {
		testschritte.remove(schritt);
		schritt.setTest(null);
	}

	public String getTestdaten() {
		return testdaten;
	}

	public void setTestdaten(String testdaten) {
		this.testdaten = testdaten;
	}

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getNotizen() {
		return notizen;
	}

	public void setNotizen(String notizen) {
		this.notizen = notizen;
	}
	
	public Set<Testlauf> getTestlaeufe() {
		return testlaeufe;
	}
	
	public void setTestlaeufe(Set<Testlauf> testlaeufe) {
		this.testlaeufe = testlaeufe;
	}

   
}