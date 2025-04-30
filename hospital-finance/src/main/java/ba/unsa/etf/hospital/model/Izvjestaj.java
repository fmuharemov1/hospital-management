package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Polje Izvještaj ne smije biti prazno")
    @Size(max = 255, message = "Polje Izvještaj može imati maksimalno 255 karaktera")
    private String tipIzvjestaja;
    @NotNull(message = "Polje Broj pacijenata ne smije biti prazno")
    @Min(0)
    private int brojPacijenata;
    @NotNull(message = "Polje Broj termina ne smije biti prazno")
    @Min(0)
    private int brojTermina;
    @NotNull(message = "Polje Financijski pregled ne smije biti prazno")
    @Min(0)
    private double finansijskiPregled;
    @NotBlank(message = "Polje Datum generisanja ne smije biti prazno")
    private Date datumGenerisanja;

    public void setFinancijskiPregled(double finansijskiPregled) {
        this.finansijskiPregled = finansijskiPregled;
    }
}
