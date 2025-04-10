package ba.unsa.etf.hospital.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Obavijest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Polje sadrzaj ne smije biti prazno")
    @Size(max = 500, message = "Polje sadrzaj ne smije imati više od 500 karaktera")  // po potrebi mijenjati
    private String sadrzaj;

    @NotNull(message = "Polje datum i vrijeme ne smije biti prazno")
    private LocalDateTime datum_vrijeme;
}
