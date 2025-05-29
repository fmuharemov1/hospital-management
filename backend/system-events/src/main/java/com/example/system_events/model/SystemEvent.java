package com.example.system_events.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // <--- OVO JE KLJUČNO
public class SystemEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String actionType;
    private String service;
    private String resource;
    private String status;
    private String username;
    private LocalDateTime timestamp;
}
