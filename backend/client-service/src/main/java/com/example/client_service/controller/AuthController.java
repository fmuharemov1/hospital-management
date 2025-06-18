package com.example.client_service.controller;
// Provjerite putanju, vjerovatno je auth_service.controller

import com.example.client_service.auth.LoginRequest;
import com.example.client_service.model.User;
import com.example.client_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth") // Putanja za AuthController, npr. /auth/login
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            User authenticatedUser = userService.authenticate(loginRequest);
            // Sada generišemo token koristeći User objekat
            String jwt = userService.generateJwtTokenForUser(authenticatedUser); // Koristite novu metodu

            return ResponseEntity.ok(new JwtResponse(jwt)); // Vraća JWT token
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    // Unutarnja klasa za JWT odgovor
    static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    // Dodajte i /register endpoint ako ga imate
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User newUser) { // Pretpostavka da primate User objekat za registraciju
        try {
            User registeredUser = userService.register(newUser);
            // Možete odmah generirati token i prijaviti ga
            String jwt = userService.generateJwtTokenForUser(registeredUser);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

