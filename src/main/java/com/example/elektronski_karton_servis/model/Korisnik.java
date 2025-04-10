package com.example.elektronski_karton_servis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name="Korisnik")
public class Korisnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Ime je obavezno")
    @Column(name="ime")
    private String ime;


    @NotBlank(message = "Prezime je obavezno")
    @Column(name="prezime")
    private String prezime;


    @NotBlank(message = "Prezime je obavezno")
    @Column(name="email")
    private String email;


    @NotBlank(message = "Lozinka je obavezna")
    @Column(name="lozinka")
    private String lozinka;

    @Column(name="br_telefona")
    private String brojTelefona;

    @Column(name="adresa")
    private String adresa;

    @Column(name="roleId")
    private Integer roleId;

    @Column(name="korisnik_uuid")
    private UUID korisnikUuid;

    public Korisnik() {
        this.korisnikUuid = UUID.randomUUID();
    }

    public Korisnik(String ime, String prezime, String email, String lozinka, String brojTelefona, String adresa, Integer roleId) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
        this.brojTelefona = brojTelefona;
        this.adresa = adresa;
        this.roleId = roleId;
        this.korisnikUuid = UUID.randomUUID();
    }
}


