package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import ba.unsa.etf.hospital.repository.SobaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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
    public ResponseEntity<?> dodijeliSobuPacijentu(Long korisnikId, Long sobaId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new EntityNotFoundException("Korisnik nije pronađen"));

        if (!korisnik.getRole().getTipKorisnika().equalsIgnoreCase("pacijent")) {
            return ResponseEntity.badRequest().body("Korisnik nije pacijent.");
        }

        Soba soba = sobaRepository.findById(sobaId)
                .orElseThrow(() -> new EntityNotFoundException("Soba nije pronađena"));

        long brojPacijenataUSobi = korisnikRepository.countByRole_Soba_Id(sobaId);

        if (brojPacijenataUSobi >= soba.getKapacitet()) {
            soba.setStatus("Zauzeta");
            sobaRepository.save(soba);
            return ResponseEntity.badRequest().body("Soba je popunjena.");
        }

        // Postavi sobu pacijentu
        korisnik.getRole().setSoba(soba);
        korisnikRepository.save(korisnik);

        // Ako je sada soba popunjena, ažuriraj status
        if (brojPacijenataUSobi + 1 >= soba.getKapacitet()) {
            soba.setStatus("Zauzeta");
            sobaRepository.save(soba);
        }

        return ResponseEntity.ok("Soba uspješno dodijeljena pacijentu.");
    }
}
