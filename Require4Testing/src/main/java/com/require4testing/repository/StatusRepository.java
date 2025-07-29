package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
}