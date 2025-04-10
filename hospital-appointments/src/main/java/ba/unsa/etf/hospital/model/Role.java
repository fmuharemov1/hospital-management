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
    private String smjena;
    private String odjeljenje;
}
