package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Anforderung;

public interface AnforderungRepository extends JpaRepository<Anforderung, Long> {
}