package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Izvjestaj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "osoblje_id")
    private Korisnik osoblje;
    @Size(max = 255, message = "Polje Izvještaj može imati maksimalno 255 karaktera")
    private String tipIzvjestaja;
    private int brojPacijenata;
    private int brojTermina;
    private double finansijskiPregled;
    private Date datumGenerisanja;

    public void setFinancijskiPregled(double finansijskiPregled) {
        this.finansijskiPregled = finansijskiPregled;
    }
}
