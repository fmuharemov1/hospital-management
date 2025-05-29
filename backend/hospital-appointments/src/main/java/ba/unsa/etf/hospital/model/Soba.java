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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBroj_sobe() {
        return broj_sobe;
    }

    public void setBroj_sobe(String broj_sobe) {
        this.broj_sobe = broj_sobe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(int kapacitet) {
        this.kapacitet = kapacitet;
    }
}
