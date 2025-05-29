package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.dto.PacijentDTO;
import com.example.logging.GrpcSystemEventsClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PacijentService {

    private final GrpcSystemEventsClient grpcClient;
    private final RestTemplate restTemplate;

    public PacijentService(GrpcSystemEventsClient grpcClient, RestTemplateBuilder builder) {
        this.grpcClient = grpcClient;
        this.restTemplate = builder.build();
    }

    public PacijentDTO dohvatiPacijenta(Long id) {
        PacijentDTO pacijent;
        String status;

        try {
            pacijent = restTemplate.getForObject(
                    "http://ELEKTRONSKI-KARTON-SERVIS/pacijenti/" + id,
                    PacijentDTO.class
            );
            status = "USPJEŠNO";
        } catch (Exception e) {
            pacijent = new PacijentDTO(id, "Nepoznato", "Pacijent");
            status = "NEUSPJEŠNO";
        }

        // Logovanje u system-events preko gRPC
        grpcClient.log(
                "GET",                        // Tip akcije
                "hospital-appointments",     // Naziv servisa
                "Pacijent",                  // Resurs
                status,                      // Status
                "admin"                      // Korisnik (možeš promijeniti ako imaš autentikaciju)
        );

        return pacijent;
    }
}
