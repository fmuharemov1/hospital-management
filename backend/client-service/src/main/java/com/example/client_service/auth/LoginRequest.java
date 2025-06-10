package com.example.client_service.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    // getteri i setteri
    private String username;
    private String password;

}
