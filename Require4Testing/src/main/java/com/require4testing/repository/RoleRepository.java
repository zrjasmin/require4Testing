package com.require4testing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.require4testing.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	public Role findByName(String name);
}