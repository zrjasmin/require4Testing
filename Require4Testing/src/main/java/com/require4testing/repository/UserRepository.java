package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}