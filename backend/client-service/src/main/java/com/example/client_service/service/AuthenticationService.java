package com.example.client_service.service;

import com.example.client_service.auth.LoginRequest;
import com.example.client_service.auth.RegisterRequest;
import com.example.client_service.auth.JwtResponse;
import com.example.client_service.model.Role;
import com.example.client_service.model.User;
import com.example.client_service.repository.UserRepository;
import com.example.client_service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public JwtResponse register(RegisterRequest request) {
        try {
            if (userRepo.findByUsername(request.getUsername()).isPresent()) {
                throw new RuntimeException("Korisnik već postoji");
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER); // Default role

            userRepo.save(user);

            String token = jwtService.generateToken(user.getUsername());
            return new JwtResponse(token);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Registracija nije uspjela: " + e.getMessage());
        }
    }



    public JwtResponse login(LoginRequest req) {
        Optional<User> optionalUser = userRepo.findByUsername(req.getUsername());

        if (!optionalUser.isPresent()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new JwtResponse(token);
    }
}
