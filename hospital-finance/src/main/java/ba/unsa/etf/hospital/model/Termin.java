package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID terminUuid;

    @ManyToOne
    @JoinColumn(name = "pacijent_uuid")
    private Korisnik pacijent;

    @ManyToOne
    @JoinColumn(name = "osoblje_uuid")
    private Korisnik osoblje;

    @ManyToOne
    @JoinColumn(name = "faktura_id")
    private Faktura faktura;

    private Date datumVrijeme;
}
