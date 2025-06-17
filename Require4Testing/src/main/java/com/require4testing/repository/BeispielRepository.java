package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Beispiel;

public interface BeispielRepository extends JpaRepository<Beispiel, Long> {
}