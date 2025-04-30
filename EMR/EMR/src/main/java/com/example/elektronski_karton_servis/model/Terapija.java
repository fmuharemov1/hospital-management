package com.example.elektronski_karton_servis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name="Terapija")
public class Terapija {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="osoblje_uuid")
    private Integer osobljeUuid;

    @NotBlank(message = "Naziv je obavezan")
    @Column(name="naziv")
    private String naziv;

    @Column(name="dijagnoza_id")
    private Integer dijagnoza_id;

    @NotBlank(message = "Opis je obavezan")
    @Column(name="opis")
    private String opis;

    @Column(name="datum_pocetka")
    private LocalDateTime datumPocetka;

    @Column(name="datum_zavrsetka")
    private LocalDateTime datumZavrsetka;

    public Terapija() {

        this.datumPocetka = LocalDateTime.now();
    }

    public Terapija(Integer osobljeUuid, String naziv, String opis, LocalDateTime datumZavrsetka) {

        this.osobljeUuid = osobljeUuid;
        this.naziv = naziv;
        this.opis = opis;
        this.datumPocetka = LocalDateTime.now();
        this.datumZavrsetka = datumZavrsetka;
    }
}






