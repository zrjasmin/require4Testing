package com.require4testing.dto;


public class TestschrittDto {
	private Long id;
	private String beschreibung;
	private Integer stepNumber;
	private TestDto test;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public Integer getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}
	public TestDto getTest() {
		return test;
	}
	public void setTest(TestDto test) {
		this.test = test;
	}
	
}