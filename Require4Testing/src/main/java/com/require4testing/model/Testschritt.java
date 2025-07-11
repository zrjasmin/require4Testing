package com.require4testing.model;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@Table(name="testschritt")
public class Testschritt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String beschreibung;
    private Integer stepNumber;
    
    @ManyToOne
    @JoinColumn(name="test_id")
    private Test test;
    
    public Testschritt() {}

    
    public Long getId() {
    	return id;
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
	
	public Test getTest() {
		return test;
	}

	public void setTest(Test t) {
		this.test = t;
	}
	
}
    
