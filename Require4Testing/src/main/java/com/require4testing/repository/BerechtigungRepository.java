package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Berechtigung;
import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;

import antlr.collections.List;

public interface BerechtigungRepository extends JpaRepository<Berechtigung, Long> {
	
	public Berechtigung findByName(String name);
	
	
}