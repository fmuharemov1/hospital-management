package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.repository.ObavijestRepository;
import ba.unsa.etf.hospital.repository.TerminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TerminService {
    private final TerminRepository terminRepository;
    private final ObavijestRepository obavijestRepository;
    public TerminService(TerminRepository terminRepository, ObavijestRepository obavijestRepository) {
        this.terminRepository = terminRepository;
        this.obavijestRepository = obavijestRepository;
    }

    public List<Termin> getAllTermini(){
        return terminRepository.findAll();
    }

    public Termin saveTermin(Termin termin) {
        return terminRepository.save(termin);
    }

    public Optional<Termin> findById(Long id) {
        return terminRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (!terminRepository.existsById(id)) {
            throw new TerminNotFoundException(id);
        }
        terminRepository.deleteById(id);
    }

    /*
        Metode servisa sa transakcijama - kreiranje termina sa dodanom notifikacijom. Automatski se podesava tekst i datum
        notifikacije i dodjeljuje se odgovarajućem terminu.
        Ocekuje se da do ovog momenta termin vec ima odgovarajuce atribute postavljene. Primjer request body:
        {
          "pacijent": { "id": 5 },
          "osoblje": { "id": 2 },
          "status": "Zakazano",
          "datumVrijeme": "2025-05-01T10:00:00",
          "trajanje": 30,
          "meet_link": "https://example.com/termin"
        }
     */
    @Transactional
    public Termin kreirajTerminSaNotifikacijom(Termin termin) {
        // Prvo kreiramo obavijest
        Obavijest obavijest = new Obavijest();

        // Podesavamo datum - 24 sata ranije
        LocalDateTime obavijestVrijeme = termin.getDatumVrijeme().minusDays(1);
        obavijest.setDatum_vrijeme(obavijestVrijeme);

        // Formatiranje sati i minuta
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String vrijemeTermina = termin.getDatumVrijeme().format(formatter);

        // Generisanje sadržaja
        Korisnik doktor = termin.getOsoblje();
        String sadrzaj = "Podsjetnik: Sutra imate pregled kod " +
                doktor.getIme() + " " + doktor.getPrezime() +
                " u " + vrijemeTermina + ".";

        obavijest.setSadrzaj(sadrzaj);

        // Snimamo obavijest
        Obavijest savedObavijest = obavijestRepository.save(obavijest);

        // Sad postavljamo obavijest u termin
        termin.setObavijest(savedObavijest);

        // Osiguramo da svaki termin ima svoj UUID
        termin.setTerminUuid(UUID.randomUUID());

        // Snimamo termin
        return terminRepository.save(termin);
    }
}
