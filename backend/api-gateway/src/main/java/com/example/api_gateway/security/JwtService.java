// api-gateway/src/main/java/com/example/api_gateway/security/JwtService.java
package com.example.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm; // Iako API Gateway ne generiše, korisno je da je ovdje zbog getSigningKey()
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.io.Decoders; // Dodajte ovaj import za Decoders

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service klasa za validaciju JWT tokena u API Gatewayu.
 * Ključno je da 'secret' bude IDENTIČAN i da se generiše/validira na IDENTIČAN način u svim servisima.
 */
@Service
public class JwtService {

    // Vrijednost tajnog ključa se injektuje iz application.properties (jwt.secret)
    @Value(value = "${jwt.secret}")
    private String secret;

    // Konstruktor za injektovanje secret ključa.
    // Ako koristite Spring Boot 2.x+, @Value u konstruktoru je preferirani način.
    // Uklonite prazan konstruktor ako ga imate, osim ako nije za specifične testove.
    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        System.out.println("--- JWT Service Init: Secret loaded (first 10 chars): " + secret.substring(0, Math.min(secret.length(), 10)) + "...");
    }

    /**
     * Dohvaća potpisni ključ iz tajnog stringa.
     * Tajni ključ mora biti Base64 kodiran.
     */
    private Key getSigningKey() {
        System.out.println("--- JWT Service DEBUG: getSigningKey() called.");
        try {
            // Decodes.BASE64 je dio jjwt-api, Base64.getDecoder() je Java standard
            // Koristite Decoders.BASE64.decode() za konzistentnost sa JJWT
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            Key key = Keys.hmacShaKeyFor(keyBytes);
            System.out.println("--- JWT Service DEBUG: Secret Key generated for signing/validation.");
            return key;
        } catch (IllegalArgumentException e) {
            System.err.println("--- JWT Service ERROR: Failed to decode Base64 secret key: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Greška servera: Neispravan JWT secret ključ konfiguracija.");
        }
    }

    /**
     * Izdvaja korisničko ime (subjekat) iz tokena.
     * @param token JWT token
     * @return korisničko ime
     */
    public String extractUsername(String token) {
        System.out.println("--- JWT Service DEBUG: extractUsername() called with token: " + token.substring(0, Math.min(token.length(), 20)) + "...");
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Provjerava da li je token validan za datog korisnika (username).
     * @param token JWT token
     * @param username korisničko ime (username) koje se očekuje u tokenu
     * @return true ako je token validan, false inače
     */
    public boolean isTokenValid(String token, String username) {
        System.out.println("--- JWT Service DEBUG: isTokenValid() called for username: " + username + " ---");
        try {
            final String extractedUsername = extractUsername(token);
            boolean isValid = (extractedUsername.equals(username) && !isTokenExpired(token));
            System.out.println("--- JWT Service DEBUG: Token validity check result: " + isValid + " for user: " + extractedUsername);
            return isValid;
        } catch (ResponseStatusException e) {
            // Već smo uhvatili specifične JWT greške u extractAllClaims, samo ih re-throwamo
            System.err.println("--- JWT Service DEBUG: isTokenValid caught ResponseStatusException: " + e.getReason());
            throw e;
        } catch (Exception e) {
            // Opća greška ako nešto prođe kroz catch blokove
            System.err.println("--- JWT Service ERROR: isTokenValid encountered unexpected error: " + e.getClass().getName() + " - " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nevažeći token: Neočekivana greška tokom validacije.");
        }
    }

    /**
     * Izdvaja određeni claim iz tokena.
     * @param token JWT token
     * @param claimsResolver funkcija za rješavanje claima
     * @param <T> tip claima
     * @return vrijednost claima
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        System.out.println("--- JWT Service DEBUG: extractClaim() called for token: " + token.substring(0, Math.min(token.length(), 20)) + "...");
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Izdvaja sve claimove (tijelo) iz tokena.
     * Ovdje se dešava stvarna validacija tokena (potpis, format, istek).
     * @param token JWT token
     * @return svi claimovi iz tokena
     * @throws ResponseStatusException ako token nije validan
     */
    private Claims extractAllClaims(String token) {
        System.out.println("--- JWT Service DEBUG: extractAllClaims() called for token: " + token.substring(0, Math.min(token.length(), 20)) + "...");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey()) // Dohvati ključ
                    .build()
                    .parseClaimsJws(token) // Ova metoda validira potpis, format i istek!
                    .getBody();
        } catch (SignatureException e) {
            System.err.println("--- JWT Service ERROR: INVALID JWT SIGNATURE: " + e.getMessage() + ". Secret used: " + secret.substring(0, Math.min(secret.length(), 10)) + "...");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je nevažeći: Neispravan potpis.");
        } catch (MalformedJwtException e) {
            System.err.println("--- JWT Service ERROR: INVALID JWT TOKEN FORMAT: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je nevažeći: Loš format.");
        } catch (ExpiredJwtException e) {
            System.err.println("--- JWT Service ERROR: EXPIRED JWT TOKEN: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je istekao.");
        } catch (UnsupportedJwtException e) {
            System.err.println("--- JWT Service ERROR: UNSUPPORTED JWT TOKEN: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je nevažeći: Nepodržan.");
        } catch (IllegalArgumentException e) {
            System.err.println("--- JWT Service ERROR: JWT CLAIMS STRING IS EMPTY/MALFORMED: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je nevažeći: Prazan ili pogrešan token.");
        } catch (Exception e) { // Hvata sve ostale neočekivane izuzetke
            System.err.println("--- JWT Service ERROR: UNKNOWN JWT PARSING ERROR: " + e.getClass().getName() + " - " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token je nevažeći: Nepoznata greška.");
        }
    }

    /**
     * Provjerava da li je token istekao.
     * Ova metoda se oslanja na extractAllClaims, gdje se većina provjera isteka dešava.
     * Ako extractAllClaims baci ExpiredJwtException, onda će se to uhvatiti.
     * Dodatna provjera 'before(new Date())' je redudantna ako parseClaimsJws već baca ExpiredJwtException.
     * Ostavit ćemo je ovdje radi konzistentnosti, ali znajte da je primarna validacija u parseClaimsJws.
     * @param token JWT token
     * @return true ako je token istekao, false inače
     */
    private boolean isTokenExpired(String token) {
        System.out.println("--- JWT Service DEBUG: Checking if token is expired.");
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (ExpiredJwtException e) {
            // Ovo se neće desiti ako je ExpiredJwtException već uhvaćen u extractAllClaims, ali za svaki slučaj
            return true;
        } catch (Exception e) {
            System.err.println("--- JWT Service ERROR: Error checking token expiration: " + e.getMessage());
            throw e; // Prosljeđujemo grešku dalje
        }
    }

    /**
     * Izdvaja uloge (roles) iz JWT tokena.
     * Pretpostavlja da su uloge spremljene kao lista stringova pod claimom 'roles' u payloadu.
     * @param token JWT token
     * @return Lista stringova koji predstavljaju uloge
     */
    public List<String> extractRoles(String token) {
        System.out.println("--- JWT Service DEBUG: extractRoles() called for token: " + token.substring(0, Math.min(token.length(), 20)) + "...");
        Claims claims = extractAllClaims(token);
        Object rolesObject = claims.get("roles");
        System.out.println("--- JWT Service DEBUG: Raw 'roles' claim object type: " + (rolesObject != null ? rolesObject.getClass().getName() : "null"));

        if (rolesObject instanceof List<?>) {
            try {
                // Pokušaj sigurne konverzije
                List<String> roles = ((List<?>) rolesObject).stream()
                        .map(Object::toString) // Pretvorite svaki element u String
                        .collect(Collectors.toList());
                System.out.println("--- JWT Service DEBUG: Successfully extracted roles: " + roles);
                return roles;
            } catch (ClassCastException e) {
                System.err.println("--- JWT Service ERROR: ClassCastException when casting roles to List<String>: " + e.getMessage());
                return Collections.emptyList();
            }
        } else if (rolesObject != null) {
            System.err.println("--- JWT Service ERROR: 'roles' claim is not a List. Actual type: " + rolesObject.getClass().getName());
        } else {
            System.out.println("--- JWT Service DEBUG: 'roles' claim is null or not present.");
        }
        return Collections.emptyList(); // Vrati praznu listu ako claim "roles" ne postoji ili nije u ispravnom formatu
    }

    // Ostavite generateToken metodu samo ako je apsolutno neophodna za API Gateway
    // U suprotnom, trebala bi biti samo u Auth Service-u.
    // U API Gatewayu nam prvenstveno treba samo validacija.
    public String generateToken(String email) {
        System.out.println("--- JWT Service WARNING: generateToken() called in API Gateway. This should ideally be in Auth Service only.");
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationTime = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10); // 10h

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationTime)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
