package com.require4testing.model;

import javax.persistence.*;

@Entity
public class Berechtigung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    
    public Berechtigung() {}
    
    public Berechtigung(String name) {
    	this.name = name;
    }

    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

    // Getters and setters
}