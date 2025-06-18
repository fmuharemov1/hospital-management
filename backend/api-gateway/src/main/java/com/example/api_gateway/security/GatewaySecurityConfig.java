package com.example.api_gateway.security;

import com.example.api_gateway.security.SecurityContextRepository;
import com.example.api_gateway.security.JwtService; // Neophodno za ReactiveAuthenticationManager
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder; // Dodatni import ako se koristi
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private final SecurityContextRepository securityContextRepository;
    private final JwtService jwtService; // Injektujte JwtService jer ga koristi ReactiveAuthenticationManager

    // KONSTRUKTOR: Mora injektovati SecurityContextRepository i JwtService
    public GatewaySecurityConfig(SecurityContextRepository securityContextRepository, JwtService jwtService) {
        this.securityContextRepository = securityContextRepository;
        this.jwtService = jwtService;
    }

    // Bean za ReactiveAuthenticationManager
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> {
            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                // Token je već validiran u SecurityContextRepository, ovdje samo potvrđujemo
                System.out.println("--- GSC: ReactiveAuthenticationManager processing token: " + authentication.getPrincipal());
                return Mono.just(authentication); // Samo prosljedi validirani Authentication objekat
            }
            System.out.println("--- GSC: ReactiveAuthenticationManager rejecting unexpected token type: " + authentication.getClass().getName());
            return Mono.error(new IllegalArgumentException("Neočekivan tip autentifikacije: " + authentication.getClass()));
        };
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // KLJUČNE LINIJE: Povežite vaš SecurityContextRepository i AuthenticationManager!
                .securityContextRepository(securityContextRepository) // <-- OVE LINIJE MORAJU BITI AKTIVNE!
                .authenticationManager(reactiveAuthenticationManager()) // <-- OVE LINIJE MORAJU BITI AKTIVNE!
                .authorizeExchange(exchanges -> exchanges
                        // Dozvoljava POST zahtjeve za registraciju i login
                        .pathMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // Dozvoljava OPTIONS zahtjeve za CORS preflight
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Dozvoljava pristup Eureka Dashboardu
                        .pathMatchers("/eureka/**").permitAll()
                        // Dozvoljava pristup Swagger/OpenAPI dokumentaciji
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()

                        // Zaštićene putanje sa ulogama
                        // Pobrinite se da se uloge u JWT-u (koje Auth Service generiše) podudaraju sa ovim.
                        // Npr. ako je u JWT-u "ROLE_USER", ovdje ide hasAnyRole("USER").
                        // Ako je u JWT-u samo "USER", onda hasAnyRole("USER").
                        // Naš JwtService u API Gatewayu sada uklanja "ROLE_" prefiks ako postoji.
                        .pathMatchers("/api/client/patients-with-invoices").hasAnyRole("USER", "ADMIN", "DOCTOR", "STAFF")
                        .pathMatchers("/api/client/invoices/**").hasAnyRole("USER", "ADMIN", "DOCTOR", "STAFF")
                        .pathMatchers("/api/client/users/me").hasAnyRole("USER", "ADMIN", "DOCTOR", "STAFF")
                        .pathMatchers("/izvjestaji/summary/**").hasAnyRole("ADMIN", "STAFF", "DOCTOR") // Prilagodite uloge!
                        .pathMatchers("/api/emr/patients/**").hasAnyRole("USER", "ADMIN", "DOCTOR", "STAFF")
                        .pathMatchers("/api/emr/medical-records/**").hasAnyRole("USER", "ADMIN", "DOCTOR", "STAFF")
                        .pathMatchers("/api/emr/kartons/**").hasAnyRole("USER", "ADMIN", "DOCTOR", "STAFF")
                        .pathMatchers("/api/client/emr/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .pathMatchers("/api/emr/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        // Svi ostali zahtjevi zahtijevaju autentifikaciju.
                        // Ovo je "catch-all" pravilo i treba biti zadnje.
                        .anyExchange().authenticated()
                );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
