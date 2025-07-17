package com.require4testing.model;

import javax.persistence.Entity;

import java.util.Set;

import javax.persistence.*;

@Entity
public class Role {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
            name = "role_berechigungen",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "berechtigung_id")
    )
	private Set<Berechtigung> berechtigungen;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Berechtigung> getBerechtigungen() {
		return berechtigungen;
	}
	
	public  void getBerechtigungen(Set<Berechtigung> b) {
		this.berechtigungen = b;
	}
	
	
	/*
	RE("Requirements Engineer"),
    TEST_MANAGER("Testmanager:in"),
    TESTER("Tester:in"),
    TEST_FALLERSTELLR("Testfallersteller:in");
	
	private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }*/
}
