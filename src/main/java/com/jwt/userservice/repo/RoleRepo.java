package com.jwt.userservice.repo;

import com.jwt.userservice.model.AppRole;

import com.jwt.userservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<AppRole, Long> {
    AppRole findByName(String name);
}
