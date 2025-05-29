package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Polje Korisnik UUID ne smije biti prazno")
    private UUID korisnikUuid;

    @NotNull(message = "Polje Role ne smije biti prazno")
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
