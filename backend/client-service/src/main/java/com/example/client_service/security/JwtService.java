package com.example.client_service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.Decoders; // KLJUČNA IZMJENA: Koristi JJWT-ov Decoders

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails; // Ostavljeno ako je potrebno za druge metode
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors; // Dodajte import za stream

import com.example.client_service.model.User; // Pretpostavka da User model postoji ovdje

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // Ažurirani konstruktor za bolji pregled ključa
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        System.out.println("--- Auth JWT Service Init: Secret loaded (first 10 chars): " + secret.substring(0, Math.min(secret.length(), 10)) + "...");
    }

    private Key getSigningKey() {
        System.out.println("--- Auth JWT Service DEBUG: getSigningKey() called.");
        try {
            // KLJUČNA IZMJENA: Koristi Decoders.BASE64.decode() za konzistentnost
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            Key key = Keys.hmacShaKeyFor(keyBytes);
            System.out.println("--- Auth JWT Service DEBUG: Secret Key generated for signing.");
            return key;
        } catch (IllegalArgumentException e) {
            System.err.println("--- Auth JWT Service ERROR: Failed to decode Base64 secret key: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Greška servera: Neispravna JWT secret ključ konfiguracija u Auth Service-u.");
        }
    }

    /**
     * Generiše JWT token za datog korisnika (User objekat).
     * Sada uključuje uloge i druge korisničke podatke kao claimove.
     * @param user User objekat (model)
     * @return generisani JWT token
     */
    public String generateToken(User user) {
        System.out.println("--- Auth JWT Service DEBUG: generateToken() called for user: " + user.getUsername());
        Map<String, Object> claims = new HashMap<>();

        // Dodajte uloge u claims
        // Pretpostavka: User model ima getRole() metodu koja vraća Role enum
        // Konvertujemo Role enum u String (npr. "ROLE_ADMIN")
        if (user.getRole() != null) {
            // Pretpostavimo da je uloga spremljena kao ROLE_NAME (npr. "ROLE_USER")
            // A extractRoles u API Gatewayu očekuje List<String>.
            // Ako je vaša uloga jednostavan String, onda:
            // claims.put("roles", Collections.singletonList(user.getRole().name()));
            // Ako je uloga složeniji objekt, prilagodite.
            claims.put("roles", Collections.singletonList("ROLE_" + user.getRole().name())); // Npr. ["ROLE_USER"]
        } else {
            claims.put("roles", Collections.emptyList());
        }

        // Dodajte ostale korisničke detalje kao custom claimove
        claims.put("fullName", user.getFullName()); // Koristi getFullName() iz User modela
        claims.put("email", user.getEmail());// Pretpostavka da User model ima getPhone()

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10); // 10h

        String token = Jwts.builder()
                .setClaims(claims) // Postavite sve claimove
                .setSubject(user.getUsername()) // Korisničko ime kao subject
                .setIssuedAt(issuedAt)
                .setExpiration(expirationTime)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("--- Auth JWT Service DEBUG: Token generated successfully.");
        return token;
    }


    public boolean isTokenValid(String token, UserDetails userDetails) { // Promijenjeno iz UserDetails username
        System.out.println("--- Auth JWT Service DEBUG: isTokenValid() called for user: " + userDetails.getUsername());
        final String extractedUsername = extractUsername(token);
        boolean isValid = (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
        System.out.println("--- Auth JWT Service DEBUG: Token validity check result: " + isValid);
        return isValid;
    }

    public String extractUsername(String token) {
        System.out.println("--- Auth JWT Service DEBUG: extractUsername() called.");
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        System.out.println("--- Auth JWT Service DEBUG: extractClaim() called.");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        System.out.println("--- Auth JWT Service DEBUG: extractAllClaims() called.");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            System.err.println("--- Auth JWT Service ERROR: INVALID JWT SIGNATURE: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nevažeći token: Neispravan potpis");
        } catch (MalformedJwtException e) {
            System.err.println("--- Auth JWT Service ERROR: INVALID JWT TOKEN: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nevažeći token: Pogrešan format");
        } catch (ExpiredJwtException e) {
            System.err.println("--- Auth JWT Service ERROR: EXPIRED JWT TOKEN: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je istekao");
        } catch (UnsupportedJwtException e) {
            System.err.println("--- Auth JWT Service ERROR: UNSUPPORTED JWT TOKEN: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nevažeći token: Nepodržan format");
        } catch (IllegalArgumentException e) {
            System.err.println("--- Auth JWT Service ERROR: JWT CLAIMS STRING IS EMPTY/MALFORMED: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nevažeći token: Prazan ili pogrešan token");
        } catch (Exception e) { // Hvata sve ostale neočekivane izuzetke
            System.err.println("--- Auth JWT Service ERROR: UNKNOWN JWT ERROR: " + e.getClass().getName() + " - " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nevažeći token: Nepoznata greška");
        }
    }

    private boolean isTokenExpired(String token) {
        System.out.println("--- Auth JWT Service DEBUG: Checking if token is expired.");
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
