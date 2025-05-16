package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.dto.PacijentDTO;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PacijentService {

    private final RestTemplate restTemplate;

    public PacijentService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public PacijentDTO dohvatiPacijenta(Long id) {
        try {
            return restTemplate.getForObject(
                    "http://ELEKTRONSKI-KARTON-SERVIS/pacijenti/" + id,
                    PacijentDTO.class
            );
        } catch (Exception e) {
            // fallback odgovor
            return new PacijentDTO(id, "Nepoznato", "Pacijent");
        }
    }
}
