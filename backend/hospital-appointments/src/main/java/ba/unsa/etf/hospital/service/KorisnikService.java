package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import ba.unsa.etf.hospital.repository.SobaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class KorisnikService {
    private final KorisnikRepository korisnikRepository;
    private final SobaRepository sobaRepository;

    public KorisnikService(KorisnikRepository korisnikRepository, SobaRepository sobaRepository) {
        this.korisnikRepository = korisnikRepository;
        this.sobaRepository = sobaRepository;
    }

    public List<Korisnik> getAllKorisnici() {
        return korisnikRepository.findAll();
    }

    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }

    public Optional<Korisnik> findById(Long id) {
        return korisnikRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (!korisnikRepository.existsById(id)) {
            throw new KorisnikNotFoundException(id);
        }
        korisnikRepository.deleteById(id);
    }

    /*
        Netrivijalni servis koji provjerava kapacitet sobe i dodjeljuje sobu pacijentu. U slučaju potrebe, ažurira se status
        sobe. Korištena PATCH metoda obzirom da se pacijentu ažurira samo soba_id.
        Ako se pacijent otpušta u tijelo zahtjeva postaviti value na null npr:
        [
          {
            "op": "replace",
            "path": "/role/soba/id",
            "value": null
          }
        ]
     */
    @Transactional
    public ResponseEntity<?> patchKorisnik(Long id, JsonPatch patch) {
        try {
            Korisnik korisnik = korisnikRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Korisnik nije pronađen"));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode patchNode = objectMapper.valueToTree(patch);

            Optional<JsonNode> sobaIdOp = StreamSupport.stream(patchNode.spliterator(), false)
                    .filter(op -> op.has("path") && op.get("path").asText().equals("/role/soba/id"))
                    .findFirst();

            if (sobaIdOp.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nedostaje soba_id."));
            }

            JsonNode valueNode = sobaIdOp.get().get("value");

            if (valueNode == null || valueNode.isNull()) {
                // Sačuvaj prethodnu sobu
                Soba prethodnaSoba = korisnik.getRole().getSoba();

                // Otpusti pacijenta iz sobe
                korisnik.getRole().setSoba(null);
                korisnikRepository.save(korisnik);

                if (prethodnaSoba != null) {
                    long brojPacijenata = korisnikRepository.countByRole_Soba_Id(prethodnaSoba.getId());
                    if (brojPacijenata < prethodnaSoba.getKapacitet()) {
                        prethodnaSoba.setStatus("Slobodna");
                        sobaRepository.save(prethodnaSoba);
                    }
                }

                return ResponseEntity.ok(Map.of(
                        "message", "Pacijent je uspješno otpušten iz sobe."
                ));
            }

            Long novaSobaId = valueNode.asLong();

            Soba soba = sobaRepository.findById(novaSobaId)
                    .orElseThrow(() -> new RuntimeException("Soba nije pronađena"));

            long brojPacijenata = korisnikRepository.countByRole_Soba_Id(novaSobaId);
            if (brojPacijenata >= soba.getKapacitet()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Soba je već popunjena."));
            }

            korisnik.getRole().setSoba(soba);
            korisnikRepository.save(korisnik);

            long brojPacijenataNakon = korisnikRepository.countByRole_Soba_Id(novaSobaId);
            if (brojPacijenataNakon == soba.getKapacitet()) {
                soba.setStatus("Popunjena");
                sobaRepository.save(soba);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Pacijent uspješno smješten u sobu " + novaSobaId
            ));
        } catch (Exception e) {
            e.printStackTrace(); // za debug
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Greška pri patchanju korisnika."));
        }
    }
}
