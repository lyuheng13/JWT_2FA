package com.jwt.userservice.service;

import com.jwt.userservice.model.*;
import java.util.List;

public interface UserService {
    AppUser saveUser(AppUser user);
    AppRole saveRole(AppRole role);
    void addRoleToUser(String username, String roleName);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}
