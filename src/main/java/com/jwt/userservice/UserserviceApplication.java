package com.jwt.userservice;

import com.jwt.userservice.service.UserService;
import com.jwt.userservice.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

@SpringBootApplication
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new AppRole(null, "USER"));
			userService.saveRole(new AppRole(null, "MANAGER"));
			userService.saveRole(new AppRole(null, "ADMIN"));
			userService.saveRole(new AppRole(null, "SUPER_ADMIN"));

			userService.saveUser(new AppUser(null, "Jack", "jack", "123456", new ArrayList<>()));
			userService.saveUser(new AppUser(null, "Cindy", "cindy123", "123456", new ArrayList<>()));

			userService.addRoleToUser("jack", "USER");
			userService.addRoleToUser("cindy123", "ADMIN");
			userService.addRoleToUser("cindy123", "SUPER_ADMIN");
		};
	}

}
