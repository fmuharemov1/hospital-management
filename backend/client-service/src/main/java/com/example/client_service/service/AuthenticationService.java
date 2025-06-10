package com.example.client_service.service;

import com.example.client_service.auth.LoginRequest;
import com.example.client_service.dto.AuthenticationResponse;
import com.example.client_service.dto.RegisterRequest;
import com.example.client_service.model.Role;
import com.example.client_service.model.User;
import com.example.client_service.repository.UserRepository;
import com.example.client_service.security.JwtService;
import com.example.logging.GrpcSystemEventsClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final GrpcSystemEventsClient grpcClient;

    /**
     * Obrađuje zahtjev za prijavu korisnika.
     * @param request LoginRequest objekat sa korisničkim imenom i lozinkom.
     * @return AuthenticationResponse sa JWT tokenom, username-om i rolom ako je prijava uspješna.
     * @throws ResponseStatusException U slučaju neuspjeha prijave sa odgovarajućim HTTP statusom.
     */
    public AuthenticationResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> {
                        grpcClient.log("LOGIN", "client-service", "/api/users/login", "ERROR", request.getUsername());
                        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Korisnik nije pronađen nakon uspješne autentifikacije.");
                    });

            String jwt = jwtService.generateToken(user.getUsername());

            grpcClient.log("LOGIN", "client-service", "/api/users/login", "SUCCESS", request.getUsername());
            // Vrati JWT token, username i rolu
            return new AuthenticationResponse(jwt, user.getUsername(), user.getRole());
            // Napomena: Za ovo je potrebno da AuthenticationResponse DTO ima polja za username i role.
            // Ako već nemaš, dodaj ih u AuthenticationResponse DTO:
            // private String username;
            // private Role role;
            // i odgovarajući konstruktor/Lombok anotacije.

        } catch (BadCredentialsException e) {
            grpcClient.log("LOGIN", "client-service", "/api/users/login", "ERROR", request.getUsername());
            System.err.println("Autentifikacija neuspješna - Pogrešno korisničko ime ili lozinka: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pogrešno korisničko ime ili lozinka.");
        } catch (UsernameNotFoundException e) {
            grpcClient.log("LOGIN", "client-service", "/api/users/login", "ERROR", request.getUsername());
            System.err.println("Autentifikacija neuspješna - Korisnik nije pronađen: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Korisnik nije pronađen.");
        } catch (DisabledException e) {
            grpcClient.log("LOGIN", "client-service", "/api/users/login", "ERROR", request.getUsername());
            System.err.println("Autentifikacija neuspješna - Nalog je onemogućen: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vaš nalog je onemogućen.");
        } catch (LockedException e) {
            grpcClient.log("LOGIN", "client-service", "/api/users/login", "ERROR", request.getUsername());
            System.err.println("Autentifikacija neuspješna - Nalog je zaključan: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vaš nalog je zaključan.");
        } catch (Exception e) {
            grpcClient.log("LOGIN", "client-service", "/api/users/login", "ERROR", request.getUsername());
            e.printStackTrace();
            System.err.println("Autentifikacija neuspješna - Neočekivana greška: " + e.getClass().getName() + " - " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Greška pri autentifikaciji: " + e.getMessage());
        }
    }

    /**
     * Obrađuje zahtjev za registraciju novog korisnika.
     * @param request RegisterRequest objekat sa podacima za registraciju.
     * @return AuthenticationResponse sa JWT tokenom, username-om i rolom ako je registracija uspješna.
     * @throws ResponseStatusException U slučaju da korisnik sa datim emailom već postoji.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        // Provjeri da li korisnik sa datim emailom već postoji
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            grpcClient.log("REGISTER", "client-service", "/api/users/register", "ERROR", request.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Korisnik sa datim emailom već postoji.");
        }

        // Kreiraj novog korisnika
        User user = new User();
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // *** Ključna izmjena za rolu: ***
        // OPREZ: Za javnu registraciju, obično se ne dozvoljava korisniku da sam bira ulogu.
        // Najsigurniji pristup je postaviti defaultnu ulogu (npr. USER/PACIJENT).
        // Ako je ovo interna aplikacija ili testiranje, možeš koristiti request.getRole().
        if (request.getRole() != null) {
            user.setRole(request.getRole()); // Postavi rolu iz zahtjeva ako je poslata
        } else {
            user.setRole(Role.USER); // Defaultna uloga ako nije poslana ili za javnu registraciju
        }

        // Sačuvaj korisnika u bazu podataka
        userRepository.save(user);

        // Generiši JWT token za novoregistrovanog korisnika
        String token = jwtService.generateToken(user.getUsername());
        grpcClient.log("REGISTER", "client-service", "/api/users/register", "SUCCESS", request.getUsername());
        // Vrati JWT token, username i rolu
        return new AuthenticationResponse(token, user.getUsername(), user.getRole());
    }

    @PostConstruct
    public void printTestHash() {
        System.out.println("Hashed lozinka123: " + passwordEncoder.encode("lozinka123"));
    }
}