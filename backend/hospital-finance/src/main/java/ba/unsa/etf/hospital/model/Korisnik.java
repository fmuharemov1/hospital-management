package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
    @Column(unique = true, nullable = false)
    private UUID korisnikUuid;

    @NotNull(message = "Polje ime je obavezno")
    private String ime;

    @NotNull(message = "Polje prezime je obavezno")
    private String prezime;

    @NotNull(message = "Email je obavezan")
    @Email(message = "Email nije validan")
    @Column(unique = true)
    private String email;

    private String telefon;

    @NotNull(message = "Polje Role ne smije biti prazno")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    public String getFullName() {
        return ime + " " + prezime;
    }

    public String getPhone() {
        return telefon != null ? telefon : "";
    }
}
