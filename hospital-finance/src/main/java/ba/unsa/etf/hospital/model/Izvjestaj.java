package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Izvjestaj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "osoblje_id")
    private Korisnik osoblje;

    private String tipIzvjestaja;
    private int brojPacijenata;
    private int brojTermina;
    private boolean finansijskiPregled;
    private Date datumGenerisanja;
}
