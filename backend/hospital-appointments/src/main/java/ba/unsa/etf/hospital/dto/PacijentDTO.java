package ba.unsa.etf.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacijentDTO {
    private Long id;
    private String ime;
    private String prezime;
    private List<Long> pacijentIds;

    // ✅ Dodaj ovaj konstruktor da bi kompajliranje prošlo
    public PacijentDTO(Long id, String ime, String prezime) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
    }
}
