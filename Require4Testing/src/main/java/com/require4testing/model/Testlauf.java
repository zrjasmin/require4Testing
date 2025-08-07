package com.require4testing.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
@Table(name="testlauf")
public class Testlauf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nr;
    @NotBlank(message = "Der Titel darf nicht leer sein.")
    @Size(min = 3, max = 100, message = "Der Titel muss zwischen 3 und 100 Zeichen lang sein.")
    private String title;
    @NotBlank(message = "Der Beschreibung darf nicht leer sein.")
    @Size(max =1000, message = "Beschreibung darf maximal 1000 Zeichen haben.")
    private String beschreibung;
    @Size(max =1000, message = "Kommentar darf maximal 1000 Zeichen haben.")
    private String kommentar;
    @NotBlank(message = "Die Testumgebung darf nicht leer sein.")
    @Size(max =1000, message = "Testumgebung darf maximal 1000 Zeichen haben.")
    private String testumgebung;
    
    @ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;
    
    
    @ManyToOne
    @JoinColumn(name="tester_id")
    private User tester;
    
    @ManyToOne
    @JoinColumn(name="ersteller_id")
    private User ersteller;
   
   
    @ManyToMany
    @JoinTable(
            name = "testfall_testlauf",
            joinColumns = @JoinColumn(name = "testlauf_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id")
    )
	private Set<Test> tests;
    
  

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

	public String getNr() {
		return nr;
	}

	public void setNr(String nr) {
		this.nr = nr;
	}

	public String getKommentar() {
		return kommentar;
	}

	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}

	public String getTestumgebung() {
		return testumgebung;
	}

	public void setTestumgebung(String testumgebung) {
		this.testumgebung = testumgebung;
	}
	
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	
	
	public User getTester() {
		return tester;
	}

	public void setTester(User user) {
		this.tester = user;
	}
	
	public User getErsteller() {
		return ersteller;
	}

	public void setErsteller(User user) {
		this.ersteller = user;
	}


	public Set<Test> getTests() {
		return tests;
	}

	public void setTests(Set<Test> test) {
		this.tests = test;
	}

   
}