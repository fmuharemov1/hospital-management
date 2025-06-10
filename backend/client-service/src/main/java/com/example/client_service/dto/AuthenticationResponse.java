package com.example.client_service.dto;

import com.example.client_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String username; // Dodato
    private Role role;
}
