package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KorisnikService {
    private final KorisnikRepository korisnikRepository;

    public KorisnikService(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
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
        // Provjeri da li korisnik postoji
        Korisnik korisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new KorisnikNotFoundException(id));  // Ako korisnik nije pronađen, baci exception

        korisnikRepository.deleteById(id);  // Ako je pronađen, obriši ga
    }
}
