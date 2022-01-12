package com.jwt.userservice.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jwt.userservice.service.*;
import com.jwt.userservice.model.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser> saveUser(@RequestBody AppUser user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/user/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<AppRole> saveRole(@RequestBody AppRole role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save")
                .toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authroizationHeader = request.getHeader(AUTHORIZATION);
        if(authroizationHeader != null && authroizationHeader.startsWith("Bearer ")) {
            try {
                //Verify the token
                String token = authroizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secretiveness".getBytes(StandardCharsets.UTF_8));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodeJWT = verifier.verify(token);

                //Get the userinfo
                String username = decodeJWT.getSubject();
                AppUser user = userService.getUser(username);

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()  + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(AppRole::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(403);

                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token missing");
        }
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}