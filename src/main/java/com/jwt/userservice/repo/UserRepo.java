package com.jwt.userservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jwt.userservice.model.AppUser;

public interface UserRepo extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
