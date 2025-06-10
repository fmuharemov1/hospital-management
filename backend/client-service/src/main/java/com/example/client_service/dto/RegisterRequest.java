package com.example.client_service.dto;

import com.example.client_service.model.Role;

public class RegisterRequest {
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private Role role; // Polje za rolu

    // Getteri i setteri
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Dodat getter za 'role' polje
    public Role getRole() {
        return role;
    }

    // Dodat setter za 'role' polje
    public void setRole(Role role) {
        this.role = role;
    }
}