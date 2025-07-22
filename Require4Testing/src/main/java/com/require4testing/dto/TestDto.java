package com.require4testing.dto;

import java.util.List;

import com.require4testing.model.Anforderung;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;

public class TestDto {
	
	 private Long id;
	 private String title;
	 private String beschreibung;
	 private String erwartetesErgebnis;
	 private String testdaten;
	 private User ersteller;
	 private User tester;
	 private Anforderung anforderung;
	 private List<TestschrittDto> testschritte;
	 
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
	public User getTester() {
		return tester;
	}
	public void setTester(User tester) {
		this.tester = tester;
	}
	public Anforderung getAnforderung() {
		return anforderung;
	}
	public void setAnforderung(Anforderung anforderung) {
		this.anforderung = anforderung;
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
	
}