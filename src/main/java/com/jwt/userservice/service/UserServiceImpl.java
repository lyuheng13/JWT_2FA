package com.jwt.userservice.service;

import com.jwt.userservice.model.AppRole;
import com.jwt.userservice.model.AppUser;
import com.jwt.userservice.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user: {}", user.getName());
        return userRepo.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        log.info("Saving new role: {}", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Saving new role {} to user {}", username, roleName);
        AppUser user = userRepo.findByUsername(username);
        AppRole role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public AppUser getUser(String username) {
        log.info("Fetching user: {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<AppUser> getUsers() {
        log.info("Fetch all users");
        return userRepo.findAll();
    }
}
