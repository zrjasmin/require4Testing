package com.require4testing.dto;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.require4testing.model.Anforderung;
import com.require4testing.model.Status;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;
import com.require4testing.model.User;

public class TestlaufDto {
	
	private Long id;
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
	public Set<Long> getTestIds() {
		return testIds;
	}
	public void setTestIds(Set<Long> tests) {
		this.testIds = tests;
	}

}