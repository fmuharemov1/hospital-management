package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Polje ime ne smije biti prazno")
    @Column(nullable = false)
    private String ime;

    @NotBlank(message = "Polje prezime ne smije biti prazno")
    @Column(nullable = false)
    private String prezime;

    @NotBlank(message = "Polje email ne smije biti prazno")
    @Email(message = "Email adresa nije validna")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Polje lozinka ne smije biti prazno")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "Lozinka mora sadržavati barem jedno veliko slovo, jedno malo slovo i jedan broj")
    private String lozinka;

    @NotBlank(message = "Polje broj telefona ne smije biti prazno")
    @Pattern(regexp = "^\\+?[0-9]{1,15}$", message = "Broj telefona nije validan")
    private String br_telefona;
}
