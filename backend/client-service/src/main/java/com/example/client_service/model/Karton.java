package com.example.client_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;


@Setter
@Getter
@Entity
@Table(name="karton")
public class Karton {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "pacijent_uuid")
    private Integer pacijentUuid;

    @Column(name="datum_kreiranja")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datumKreiranja;

    @Column(name="broj_kartona")
    private String brojKartona;

    public Karton() {
        this.datumKreiranja = LocalDateTime.now();

    }

    public Karton(String brojKartona) {

        this.datumKreiranja = LocalDateTime.now();
        this.brojKartona = brojKartona;
        System.out.println("Generisani UUID: " + this.pacijentUuid);
    }

    public void setPacijentId(Long pacijentId) {
        this.pacijentUuid = pacijentId.intValue(); // konverzija Long -> Integer
    }

    public Long getPacijentId() {
        return pacijentUuid.longValue();
    }


}




