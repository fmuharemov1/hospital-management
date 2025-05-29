package com.example.client_service.model;

import jakarta.persistence.*;
import com.example.client_service.model.Role;
import lombok.*;

@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Getteri i setteri
    @Setter
    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Getter
    @Column(nullable = false)
    private String password;



    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

}
