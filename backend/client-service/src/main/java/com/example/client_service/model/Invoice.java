package com.example.client_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private String department;
    private String doctor;
    private Double price;
    private Boolean paid;

    @ManyToOne
    @JoinColumn(name = "patient_id") // fizička kolona u bazi
    private Patient patient;

}
