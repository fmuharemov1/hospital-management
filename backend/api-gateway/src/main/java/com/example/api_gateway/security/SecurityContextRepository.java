package com.example.api_gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private final JwtService jwtService;
    @Autowired
    public SecurityContextRepository(@Lazy JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        System.out.println("--- SCR DEBUG: load() method started for request path: " + exchange.getRequest().getPath().value() + " ---");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            System.out.println("--- SCR: Authorization header found: " + authHeader.substring(0, Math.min(authHeader.length(), 30)) + "...");
            String authToken = authHeader.substring(7);

            try {
                String username = jwtService.extractUsername(authToken);
                List<String> roles = jwtService.extractRoles(authToken); // Dobivamo uloge (npr. ["ROLE_USER"])

                if (username != null && jwtService.isTokenValid(authToken, username)) {
                    System.out.println("--- SCR: Token is VALID for username: " + username + " with roles: " + roles + " ---");
                    // KLJUČNA IZMJENA OVDJE: Samo mapirajte postojeće uloge u SimpleGrantedAuthority
                    // Bez dodavanja "ROLE_" prefiksa jer ga auth-service već dodaje!
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new) // <-- OVDJE JE PROMJENA!
                            .collect(Collectors.toList());

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            username,
                            authToken, // Lozinka (credentials) je JWT token
                            authorities
                    );
                    return Mono.just(new SecurityContextImpl(auth));
                } else {
                    System.out.println("--- SCR: Token NOT valid or username is null after extraction ---");
                }
            } catch (ResponseStatusException e) {
                System.err.println("--- SCR ERROR (ResponseStatusException): " + e.getMessage() + " ---");
                return Mono.error(e); // Prosljeđuje specifičnu 401/grešku iz JwtService
            } catch (Exception e) {
                System.err.println("--- SCR ERROR (Generic Exception): " + e.getClass().getName() + " - " + e.getMessage() + " ---");
                return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Greška pri obradi tokena."));
            }
        } else {
            System.out.println("--- SCR: Authorization header MISSING or not Bearer token ---");
        }
        return Mono.empty(); // Ako nema tokena, ili nije Bearer, ili je prazan
    }
}

