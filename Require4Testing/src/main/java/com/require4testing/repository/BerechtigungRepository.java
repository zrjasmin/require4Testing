package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Berechtigung;

public interface BerechtigungRepository extends JpaRepository<Berechtigung, Long> {
}