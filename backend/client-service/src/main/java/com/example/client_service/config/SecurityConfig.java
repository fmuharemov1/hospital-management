// File: src/main/java/com/example/client_service/config/SecurityConfig.java
package com.example.client_service.config;

import com.example.client_service.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource; // Keep this import for CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Keep this import for UrlBasedCorsConfigurationSource

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService; // Injektuje se UserDetailsService

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // KLJUČNA IZMJENA ZA CORS: Uklanjamo CorsConfigurationSource bean i inliniramo ga ovdje.
                // Ovo pomaže da se izbjegnu dupli CORS headeri.
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Provjeri ovaj port
                    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                    configuration.setAllowCredentials(true);
                    // Nema potrebe za UrlBasedCorsConfigurationSource ovdje, jer se CorsConfiguration direktno koristi
                    return configuration;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Eksplicitno dozvoli POST zahtjeve za registraciju i login
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        // Dozvoli OPTIONS zahtjeve za CORS preflight - važno za React pozive
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated() // Svi ostali zahtjevi zahtijevaju autentifikaciju
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Koristi stateless sesije za JWT
                )
                .authenticationProvider(authenticationProvider()) // Koristi definisani bean
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Dodaj JWT filter

        return http.build();
    }

    // OBAVEZNO: AKO KORISTIŠ INLINIRANU CORS KONFIGURACIJU IZNAD, UKLONI OVU METODU POTPUNO!
    // Ako ostane, opet ćeš imati dupli CORS header.
    /*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("CORS Configuration Source bean is being created.");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    */
}