// File: src/main/java/com/example/client_service/security/JwtAuthenticationFilter.java
package com.example.client_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull; // Zadržavamo @NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    // Putanje koje ne treba da se filtriraju (tj. koje su public)
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/api/users/register",
            "/api/users/login"
            // Dodaj ovde i druge javne putanje ako ih imaš
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // OPTIMIZACIJA I KRITIČNA PROVJERA: Ako je putanja javna, odmah preskoči filter i nastavi lanac.
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Ako nema Authorization headera ili ne počinje sa "Bearer ", nastavi dalje.
        // Spring Security će uhvatiti ovo kasnije ako ruta nije public.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        // Logovanje za debugiranje
        System.out.println("JwtAuthenticationFilter: Validating token for JWT: " + jwt);
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Logovanje greške ako je token nevažeći
            System.err.println("JwtAuthenticationFilter: Error extracting username from token: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
            response.getWriter().write("{\"message\": \"Nevažeći token\"}");
            return; // Prekini lanac filtera ako je token nevažeći
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // KRITIČNA IZMJENA: jwtService.isTokenValid() očekuje UserDetails objekat.
            // Ovdje je bila greška ako je tvoja isTokenValid metoda očekivala String.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // credentials su null jer smo već autentifikovali tokenom
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("JwtAuthenticationFilter: User authenticated: " + username);
            } else {
                System.out.println("JwtAuthenticationFilter: Token is invalid for user: " + username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
                response.getWriter().write("{\"message\": \"Token je nevažeći\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    // OVO JE KLJUČNA METODA ZA PRESKAKANJE FILTERA!
    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) throws ServletException {
        // Provjerava da li je trenutna putanja prisutna u listi javnih URL-ova.
        // Upoređuje putanju zahtjeva sa početkom (prefixom) javnih URL-ova.
        String requestUri = request.getRequestURI();
        boolean skipFilter = PUBLIC_URLS.stream().anyMatch(requestUri::startsWith);
        System.out.println("JwtAuthenticationFilter.shouldNotFilter for " + requestUri + ": " + skipFilter);
        return skipFilter;
    }
}