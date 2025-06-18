package com.example.client_service.service; // Provjerite putanju, vjerovatno je auth_service.service

import com.example.client_service.model.User; // Provjerite putanju do User modela
import com.example.client_service.repository.UserRepository;
import com.example.client_service.auth.LoginRequest; // Provjerite putanju do LoginRequest
import com.example.client_service.security.JwtService; // Dodajte ovaj import
import jakarta.transaction.Transactional; // Provjerite da li je Jakarta anotacija ili javax
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired // Dodajte injektovanje JwtService
    private JwtService jwtService;

    public User authenticate(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Korisnik nije pronađen"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Pogrešni podaci za prijavu");
        }

        return user; // Vraća User objekat
    }

    // Dodajte metodu za registraciju ako je nemate
    public User register(User newUser) {
        // Dodajte logiku za provjeru duplikata username/email prije spremanja
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    // Metoda za generisanje tokena izvan autentifikacije, ako je potrebno
    public String generateJwtTokenForUser(User user) {
        return jwtService.generateToken(user); // Koristi novi generateToken metod
    }
}
