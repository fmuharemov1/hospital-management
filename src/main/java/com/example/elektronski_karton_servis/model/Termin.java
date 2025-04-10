package com.example.elektronski_karton_servis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name="Termin")
public class Termin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="pacijent_uuid")
    private Integer pacijentUuid;

    @Column(name="osoblje_uuid")
    private Integer osobljeUuid;

    @NotNull(message = "Datum i vrijeme je obavezno")
    @Column(name="datum_vrijeme")
    private LocalDateTime datumVrijeme;

    @Column(name="termin_uuid")
    private Integer termintUuid;

    public Termin() {

    }

    public Termin(Integer pacijentUuid, Integer osobljeUuid, LocalDateTime datumVrijeme) {
        this.pacijentUuid = pacijentUuid;
        this.osobljeUuid = osobljeUuid;
        this.datumVrijeme = datumVrijeme;
    }
}



