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
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
		"com.example.client_service",
		"com.example.logging"
})
public class ClientServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ClientServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate loadBalancedRestTemplate() {
		return new RestTemplate();  // Za mikroservise
	}
	@Bean
	@Primary
	public RestTemplate restTemplate() {
		return new RestTemplate();  // Za direktne URL pozive
	}
	@Bean
	@Transactional
	public CommandLineRunner initDefaultUsers(UserRepository userRepo, PasswordEncoder encoder) {
		return args -> {
			try {
				if (userRepo.findByUsername("user").isEmpty()) {
					User user = new User();
					user.setName("User");
					user.setSurname("Demo");
					user.setEmail("user@example.com");
					user.setUsername("user");
					user.setPassword(encoder.encode("user123"));
					user.setRole(Role.USER);
					userRepo.save(user);
					logger.info("Default user added.");
				}

				if (userRepo.findByUsername("admin").isEmpty()) {
					User admin = new User();
					admin.setName("Admin");
					admin.setSurname("System");
					admin.setEmail("admin@example.com");
					admin.setUsername("admin");
					admin.setPassword(encoder.encode("admin123"));
					admin.setRole(Role.ADMIN);
					userRepo.save(admin);
					logger.info("Default admin added.");
				}
			} catch (Exception e) {
				logger.error("Error while initializing default users: {}", e.getMessage());
			}
		};
	}
}
