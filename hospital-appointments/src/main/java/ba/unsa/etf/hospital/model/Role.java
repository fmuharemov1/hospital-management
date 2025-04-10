package ba.unsa.etf.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Polje Tip korisnika ne smije biti prazno")
    private String tipKorisnika; // za sada ova tri atributa kao string, u budućnosti vjerovatno enum
    @NotBlank(message = "Polje smjena ne smije biti prazno")
    private String smjena;
    @NotBlank(message = "Polje odjeljenje ne smije biti prazno")
    private String odjeljenje;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipKorisnika() {
        return tipKorisnika;
    }

    public void setTipKorisnika(String tipKorisnika) {
        this.tipKorisnika = tipKorisnika;
    }

    public String getSmjena() {
        return smjena;
    }

    public void setSmjena(String smjena) {
        this.smjena = smjena;
    }

    public String getOdjeljenje() {
        return odjeljenje;
    }

    public void setOdjeljenje(String odjeljenje) {
        this.odjeljenje = odjeljenje;
    }
}
