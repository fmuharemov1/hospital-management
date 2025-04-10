package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Soba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Polje broj sobe ne smije biti prazno")
    @Column(nullable = false)
    private String broj_sobe;

    @NotBlank(message = "Polje status ne smije biti prazno")
    @Column(nullable = false)
    private String status;  // za sada string zbog fleksibilnosti, idealno vjerujem enum

    @NotNull(message = "Polje kapacitet ne smije biti null")
    private int kapacitet;

    @ManyToOne
    @JoinColumn(name = "pacijent_id", referencedColumnName = "id", nullable = false)
    private Korisnik korisnik;
}
