package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.dto.TerminDTO;
import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import ba.unsa.etf.hospital.repository.ObavijestRepository;
import ba.unsa.etf.hospital.repository.TerminRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TerminService {
    private final TerminRepository terminRepository;
    private final ObavijestRepository obavijestRepository;
    private final KorisnikRepository korisnikRepository;

    public TerminService(TerminRepository terminRepository, ObavijestRepository obavijestRepository, KorisnikRepository korisnikRepository) {
        this.terminRepository = terminRepository;
        this.obavijestRepository = obavijestRepository;
        this.korisnikRepository = korisnikRepository;
    }

    public List<Termin> getAllTermini(){
        return terminRepository.findAll();
    }

    public List<TerminDTO> getAllTerminiDTO() {
        return terminRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
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

    @Transactional
    public Termin kreirajTerminSaNotifikacijom(Termin termin) {
        Obavijest obavijest = new Obavijest();
        LocalDateTime obavijestVrijeme = termin.getDatumVrijeme().minusDays(1);
        obavijest.setDatum_vrijeme(obavijestVrijeme);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String vrijemeTermina = termin.getDatumVrijeme().format(formatter);

        Korisnik doktor = korisnikRepository.findById(termin.getOsoblje().getId())
                .orElseThrow(() -> new RuntimeException("Doktor nije pronađen"));
        String sadrzaj = "Podsjetnik: Sutra imate pregled kod " +
                doktor.getIme() + " " + doktor.getPrezime() +
                " u " + vrijemeTermina + ".";

        obavijest.setSadrzaj(sadrzaj);
        Obavijest savedObavijest = obavijestRepository.save(obavijest);
        termin.setObavijest(savedObavijest);
        termin.setTerminUuid(UUID.randomUUID());

        return terminRepository.save(termin);
    }

    private TerminDTO mapToDTO(Termin termin) {
        String datum = termin.getDatum();
        String pocetak = termin.getVrijemePocetka();
        String kraj = termin.getVrijemeKraja();

        return new TerminDTO(
                termin.getId(),
                datum,
                pocetak,
                kraj,
                termin.getPacijent() != null ? termin.getPacijent().getId() : null,
                termin.getOsoblje() != null ? termin.getOsoblje().getId() : null
        );
    }
}
