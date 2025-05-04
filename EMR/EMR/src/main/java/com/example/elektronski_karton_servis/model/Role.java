package com.example.elektronski_karton_servis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="Role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Tip korisnika obavezan")
    @Column(name="TipKorisnika")
    private String tipKorisnika;

    @NotBlank(message = "Smjena obavezna")
    @Column(name="smjena")
    private String smjena;

    @NotBlank(message = "Odjeljenje obavezno")
    @Column(name="odjeljenje")
    private String odjeljenje;

    public Role() {
    }

    public Role(String tipKorisnika, String smjena, String odjeljenje) {
        this.tipKorisnika = tipKorisnika;
        this.smjena = smjena;
        this.odjeljenje = odjeljenje;
    }
}




