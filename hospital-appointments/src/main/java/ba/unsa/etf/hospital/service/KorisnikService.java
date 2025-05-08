package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import ba.unsa.etf.hospital.repository.SobaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
     */
    @Transactional
    public ResponseEntity<?> patchKorisnik(Long id, JsonPatch patch) {
        try {
            Korisnik korisnik = korisnikRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Korisnik nije pronađen"));

            // Primijeni patch
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode korisnikNode = objectMapper.valueToTree(korisnik);
            JsonNode patchedNode = patch.apply(korisnikNode);
            Korisnik patchedKorisnik = objectMapper.treeToValue(patchedNode, Korisnik.class);

            korisnikRepository.save(patchedKorisnik);

            return ResponseEntity.ok(patchedKorisnik);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Greška pri patchanju korisnika");
        }
    }
}
