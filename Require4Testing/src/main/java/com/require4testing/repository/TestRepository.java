package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Test;

public interface TestRepository extends JpaRepository<Test, Long> {
}