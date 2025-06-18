package com.example.client_service.dto;

// client-service/src/main/java/com/example/client_service/dto/UserProfileDto.java

import java.util.List;

// Koristimo Lombok anotacije za gettere, settere, konstruktore
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String fullName; // Ovo će biti kombinacija imena i prezimena
    private String email;
    private List<String> roles; // Uloge korisnika
}

