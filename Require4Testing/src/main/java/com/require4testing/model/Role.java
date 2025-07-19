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

	public Role() {}
	
	public Role(String name, Set<Berechtigung> berechtigungen) {
		this.name = name;
		this.berechtigungen = berechtigungen;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Berechtigung> getBerechtigungen() {
		return berechtigungen;
	}
	
	public  void setBerechtigungen(Set<Berechtigung> b) {
		this.berechtigungen = b;
	}
	
	
	
}
