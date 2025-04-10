package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private UUID terminUuid;

    @NotNull(message = "Polje Pacijent ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "pacijent_uuid")
    private Korisnik pacijent;

    @NotNull(message = "Polje Osoblje ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "osoblje_uuid")
    private Korisnik osoblje;

    @NotNull(message = "Polje Faktura ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "faktura_id")
    private Faktura faktura;

    @NotNull(message = "Polje Datum i vrijeme ne smije biti prazno")
    private LocalDateTime datumVrijeme;
}
