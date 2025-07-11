package com.require4testing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.require4testing.model.Test;
import com.require4testing.model.Testschritt;

public interface TestschrittRepository extends JpaRepository<Testschritt, Long> {
	List<Testschritt> findByTestOrderByStepNumberAsc(Test test);
}