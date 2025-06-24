package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.require4testing.model.Akzeptanzkriterium;

public interface AkzeptanzkriteriumRepository extends JpaRepository<Akzeptanzkriterium, Long> {
}