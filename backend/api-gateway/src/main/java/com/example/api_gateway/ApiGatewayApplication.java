package com.example.api_gateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

// Isključujemo SVE Servlet-bazirane sigurnosne auto-konfiguracije
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyAutoConfiguration;

// Za svaki slučaj, isključimo i WebMVC auto-konfiguracije ako su negdje ostale
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(
		// Uklanjamo scanBasePackages ako nije apsolutno neophodno,
		// jer Spring Boot podrazumijevano skenira com.example.api_gateway i podpakete.
		// Ako Vam logging-core i dalje treba i nije moguće ukloniti njegov MVC dio,
		// moramo razmisliti o drugom rješenju ili uklanjanju logging-core.
		// Za sada, pretpostavljamo da je osnovni problem Servlet Security.
		exclude = {
				SecurityAutoConfiguration.class,
				UserDetailsServiceAutoConfiguration.class,
				OAuth2ClientAutoConfiguration.class,
				OAuth2ResourceServerAutoConfiguration.class,
				Saml2RelyingPartyAutoConfiguration.class,
				WebMvcAutoConfiguration.class // Ovo bi trebalo uhvatiti bilo kakvu auto-konfiguraciju MVC-a
		}
)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			System.out.println("--- API Gateway Application Context Started ---");
			System.out.println("Let's inspect the beans provided by Spring Boot:");

			try {
				ctx.getBean(com.example.api_gateway.security.GatewaySecurityConfig.class);
				System.out.println("--> GatewaySecurityConfig bean found: OK");
			} catch (Exception e) {
				System.err.println("--> GatewaySecurityConfig bean NOT found: ERROR - " + e.getMessage());
			}

			try {
				ctx.getBean(com.example.api_gateway.security.JwtService.class);
				System.out.println("--> JwtService bean found: OK");
			} catch (Exception e) {
				System.err.println("--> JwtService bean NOT found: ERROR - " + e.getMessage());
			}

			try {
				// Ovo je ključna provjera za WebFlux SecurityContextRepository
				ctx.getBean(org.springframework.security.web.server.context.ServerSecurityContextRepository.class);
				System.out.println("--> ServerSecurityContextRepository (our custom) bean found: OK");
			} catch (Exception e) {
				System.err.println("--> ServerSecurityContextRepository (our custom) bean NOT found: ERROR - " + e.getMessage());
				// Dodatna provjera po našem tipu klase ako prvi ne uspije
				try {
					ctx.getBean(com.example.api_gateway.security.SecurityContextRepository.class);
					System.err.println("--> Our custom SecurityContextRepository bean found by class type: OK (but not by expected interface? - this is a problem)");
				} catch (Exception innerE) {
					System.err.println("--> Our custom SecurityContextRepository bean NOT found by class type either: ERROR - " + innerE.getMessage());
				}
			}
			System.out.println("----------------------------------------------");
		};
	}
}
