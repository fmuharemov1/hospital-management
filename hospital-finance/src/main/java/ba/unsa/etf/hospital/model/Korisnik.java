package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Korisnik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID korisnikUuid;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
