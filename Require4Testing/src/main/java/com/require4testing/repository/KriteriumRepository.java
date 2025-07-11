package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.require4testing.model.Kriterium;


public interface KriteriumRepository extends JpaRepository<Kriterium, Long> {
}