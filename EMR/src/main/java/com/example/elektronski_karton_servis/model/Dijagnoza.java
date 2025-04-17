package com.example.elektronski_karton_servis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name="Dijagnoza")
public class Dijagnoza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="karton_id")
    private Integer kartonId;

    @Column(name="osoblje_uuid")
    private Integer osobljeUuid;

    @NotBlank(message = "Naziv je obavezan")
    @Column(name="naziv")
    private String naziv;

    @NotBlank(message = "Opis je obavezan")
    @Column(name="opis")
    private String opis;

    @NotNull(message = "Datum dijagnoze je obavezan")
    @Column(name="datum_dijagnoze")
    private LocalDateTime datumDijagnoze;

    public Dijagnoza() {
        this.datumDijagnoze = LocalDateTime.now();

    }

    public Dijagnoza(Integer kartonId, String naziv, String opis) {
        this.kartonId = kartonId;
        this.naziv = naziv;
        this.opis = opis;
        this.datumDijagnoze = LocalDateTime.now();

    }


}


