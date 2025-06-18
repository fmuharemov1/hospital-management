package com.example.api_gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * Globalna CORS konfiguracija za Spring Cloud Gateway aplikaciju.
 * Ova klasa definira pravila za cross-origin pristup API-jima kroz gateway.
 */
@Configuration
@EnableWebFlux
public class CorsGlobalConfig implements WebFluxConfigurer {

    /**
     * Konfiguruje globalne CORS postavke za sve endpointe gateway-a.
     * Ovaj metod se poziva automatski tokom inicijalizacije aplikacije.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://127.0.0.1:3000"
                )
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS",
                        "PATCH",
                        "HEAD"
                )
                .allowedHeaders(
                        "Authorization",
                        "Content-Type",
                        "Accept",
                        "X-Requested-With",
                        "Origin",
                        "Access-Control-Request-Method",
                        "Access-Control-Request-Headers"
                )
                .exposedHeaders(
                        "Authorization",
                        "Content-Type",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials"
                )
                .allowCredentials(true)
                .maxAge(3600); // Cache CORS konfiguraciju za 1 sat
    }

    /**
     * Kreira WebFilter koji će primijeniti CORS konfiguraciju na sve zahtjeve.
     * Ovaj filter je potreban za pravilno rukovanje CORS zahtjevima u reaktivnom okruženju.
     */
    @Bean
    public CorsWebFilter corsWebFilter(Environment env) {
        CorsConfiguration config = new CorsConfiguration();

        // Konfiguracija dozvoljenih izvora
        List<String> allowedOrigins = Arrays.asList(
                "http://localhost:3000",
                "http://127.0.0.1:3000"
        );
        config.setAllowedOrigins(allowedOrigins);

        // Konfiguracija dozvoljenih metoda
        config.setAllowedMethods(Arrays.asList(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "OPTIONS",
                "PATCH",
                "HEAD"
        ));

        // Konfiguracija dozvoljenih zaglavlja
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Requested-With",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // Dozvoli slanje credentials-a (kolačići, autorizacijski tokeni)
        config.setAllowCredentials(true);

        // Postavi maksimalno vrijeme cachinga CORS konfiguracije
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}