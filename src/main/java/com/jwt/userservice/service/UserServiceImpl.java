package com.jwt.userservice.service;

import com.jwt.userservice.model.AppRole;
import com.jwt.userservice.model.AppUser;
import com.jwt.userservice.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving new user: {}", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);

        if (user == null) {
            log.error("Does not found any user");
            throw new UsernameNotFoundException("No user found");
        } else {
            log.info("Found user");
        }

        Collection<SimpleGrantedAuthority> authories = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authories.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new User(user.getUsername(), user.getPassword(), authories);
    }
}
