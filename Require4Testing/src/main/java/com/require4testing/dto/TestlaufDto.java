package com.require4testing.dto;

import java.util.List;
import java.util.Set;

import com.require4testing.model.Anforderung;
import com.require4testing.model.Status;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;

public class TestlaufDto {
	
	private Long id;
    
    private String title;
    private String beschreibung;
    private String kommentar;
    private String testumgebung;
    private Status status;
    private User tester;
    private Set<Long> testIds;
    
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
	public void setTester(User tester) {
		this.tester = tester;
	}
	public Set<Long> getTests() {
		return testIds;
	}
	public void setTests(Set<Long> tests) {
		this.testIds = tests;
	}

}