// File: src/main/java/com/example/client_service/auth/AuthenticationController.java
package com.example.client_service.auth;

import com.example.client_service.dto.AuthenticationResponse;
import com.example.client_service.dto.RegisterRequest;
import com.example.client_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        // Uklonjeni su debug ispis
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginRequest request) {
        // Uklonjeni su debug ispis
        return ResponseEntity.ok(authService.authenticate(request));
    }
}