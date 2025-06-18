package com.example.client_service.controller;

// client-service/src/main/java/com/example/client_service/controller/UserController.java

import com.example.client_service.dto.UserProfileDto; // Importirajte UserProfileDto
import com.example.client_service.model.User;
import com.example.client_service.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/users") // Ova putanja će biti /api/client/users/me
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Endpoint za dohvat profila prijavljenog korisnika
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Provjerava da li je korisnik autentifikovan
    public ResponseEntity<UserProfileDto> getMyProfile(Authentication authentication) {
        // "authentication.getName()" će vratiti "sub" claim iz JWT-a, što bi trebalo biti korisničko ime
        String username = authentication.getName();

        // Dohvati uloge iz Authentication objekta
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Pretpostavlja da su uloge u formatu "ROLE_ADMIN", "ROLE_USER"
                .map(role -> role.replace("ROLE_", "")) // Ukloni "ROLE_" prefiks ako ga ne želite slati na frontend
                .collect(Collectors.toList());

        // Dohvati korisnika iz baze podataka koristeći korisničko ime
        // Pretpostavka: imate metodu findByUsername u UserRepository
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Kreirajte UserProfileDto objekt i popunite ga podacima iz User entiteta
            UserProfileDto profile = new UserProfileDto(
                    user.getUsername(),
                    (String) user.getFullName(), // Koristi public getFullName() metodu iz User entiteta
                    user.getEmail(),
                    roles
            );
            return ResponseEntity.ok(profile);
        } else {
            // Ako korisnik nije pronađen u bazi (što se ne bi trebalo desiti ako je token validan i mapiran na korisnika)
            return ResponseEntity.notFound().build();
        }
    }
}
