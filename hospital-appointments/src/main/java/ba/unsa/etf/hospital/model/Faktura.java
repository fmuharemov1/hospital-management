package ba.unsa.etf.hospital.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Faktura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Polje iznos ne smije biti prazno")
    @Positive(message = "Iznos mora biti veći od 0 KM")
    private double iznos;
    @Size(max = 255, message = "Polje status može imati maksimalno 255 karaktera")
    @NotBlank(message = "Polje status ne smije biti prazno")
    private String status;
    @Size(max = 255, message = "Polje metod može imati maksimalno 255 karaktera")
    @NotBlank(message = "Polje metod ne smije biti prazno")
    private String metod;
}
