package com.example.client_service;

import com.example.client_service.model.Role;
import com.example.client_service.model.User;
import com.example.client_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
		"com.example.client_service",
		"com.example.logging" // da vidi interceptor
})
public class ClientServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	CommandLineRunner addUsers(UserRepository userRepo, PasswordEncoder encoder) {
		return args -> {
			if (userRepo.findByUsername("user").isEmpty()) {
				userRepo.save(new User(null, "user", encoder.encode("user123"), Role.USER));
			}
			if (userRepo.findByUsername("admin").isEmpty()) {
				userRepo.save(new User(null, "admin", encoder.encode("admin123"), Role.ADMIN));
			}
		};
	}



}
