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
    @JoinColumn(name = "pacijent_id")
    private Korisnik pacijent;

    @NotNull(message = "Polje Osoblje ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "osoblje_id")
    private Korisnik osoblje;

    @NotNull(message = "Polje obavijest_id ne smije biti prazno")
    @OneToOne
    @JoinColumn(name = "obavijest_id")
    private Obavijest obavijest;

    @NotNull(message = "Polje status ne smije biti prazno")
    private String status;

    @NotNull(message = "Polje Datum i vrijeme ne smije biti prazno")
    private LocalDateTime datumVrijeme;

    @NotNull(message = "Polje trajanje ne smije biti prazno")
    private Integer trajanje;

    private String meet_link;
}
