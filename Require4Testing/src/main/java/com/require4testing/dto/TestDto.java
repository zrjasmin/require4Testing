package com.require4testing.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


import com.require4testing.model.User;

public class TestDto {
	
	 private Long id;
	 @NotBlank(message = "Der Titel darf nicht leer sein.")
	 @Size(min = 3, max = 100, message = "Der Titel muss zwischen 3 und 100 Zeichen lang sein.")
	 private String title;
	 @Size(max =1000, message = "Beschreibung darf maximal 1000 Zeichen haben.")
	 private String beschreibung;
	 @Size(max =200, message = "Erwartetes Ergebnis darf maximal 200 Zeichen haben.")
	 private String erwartetesErgebnis;
	 @Size(max =1000, message = "Testdaten darf maximal 1000 Zeichen haben.")
	 private String testdaten;
	 private User ersteller;
	 private Long anforderungId;
	 private List<TestschrittDto> testschritte;
	 @Size(max =1000, message = "Notizen darf maximal 1000 Zeichen haben.")
	 private String notizen;
	 
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
	public String getErwartetesErgebnis() {
		return erwartetesErgebnis;
	}
	public void setErwartetesErgebnis(String erwartetesErgebnis) {
		this.erwartetesErgebnis = erwartetesErgebnis;
	}
	public User getErsteller() {
		return ersteller;
	}
	public void setErsteller(User ersteller) {
		this.ersteller = ersteller;
	}

	public Long getAnforderungId() {
		return anforderungId;
	}
	public void setAnforderungId(Long anforderungId) {
		this.anforderungId = anforderungId;
	}
	public List<TestschrittDto> getTestschritte() {
		return testschritte;
	}
	public void setTestschritte(List<TestschrittDto> testschritte) {
		this.testschritte = testschritte;
	}
	public String getTestdaten() {
		return testdaten;
	}
	public void setTestdaten(String testdaten) {
		this.testdaten = testdaten;
	}
	public String getNotizen() {
		return notizen;
	}
	public void setNotizen(String notizen) {
		this.notizen = notizen;
	}
	
}