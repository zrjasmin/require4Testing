package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Testlauf;

public interface TestlaufRepository extends JpaRepository<Testlauf, Long> {
}