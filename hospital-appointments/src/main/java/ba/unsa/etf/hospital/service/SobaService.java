package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.SobaNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import ba.unsa.etf.hospital.repository.SobaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SobaService {
    private final SobaRepository sobaRepository;
    private final KorisnikRepository korisnikRepository;

    public SobaService(SobaRepository sobaRepository, KorisnikRepository korisnikRepository) {
        this.sobaRepository = sobaRepository;
        this.korisnikRepository = korisnikRepository;
    }

    public List<Soba> getAllSobe() {
        return sobaRepository.findAll();
    }

    public Soba saveSoba(Soba soba) {
        return sobaRepository.save(soba);
    }

    public Optional<Soba> findById(Long id) {
        return sobaRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (!sobaRepository.existsById(id)) {
            throw new SobaNotFoundException(id);
        }
        sobaRepository.deleteById(id);
    }

    /*
        Ispod su servisne metode za mass dodavanje i otpuštanje pacijenata u/iz određenu sobu. Koristi se batch unos
        zbog potencijalno velikog broja pacijenata. Sadrže i validacije vezane za kapacitet soba kao i eventualno
        ažuriranje statusa soba.
     */
    @Transactional
    public void dodajPacijenteUSobu(Long sobaId, List<Long> pacijentIds) {
        Optional<Soba> sobaOptional = sobaRepository.findById(sobaId);
        if (sobaOptional.isEmpty()) {
            throw new RuntimeException("Soba nije pronađena");
        }

        Soba soba = sobaOptional.get();
        long trenutnoPacijenata = korisnikRepository.countByRole_Soba_Id(sobaId);

        if (trenutnoPacijenata + pacijentIds.size() > soba.getKapacitet()) {
            throw new RuntimeException("Nema dovoljno mjesta u sobi");
        }

        List<Korisnik> pacijenti = korisnikRepository.findAllById(pacijentIds);
        for (Korisnik pacijent : pacijenti) {
            if (pacijent.getRole() == null) {
                throw new RuntimeException("Pacijent nema dodijeljenu rolu");
            }
            pacijent.getRole().setSoba(soba);
        }
        korisnikRepository.saveAll(pacijenti);

        // Update status ako je soba puna
        long brojPacijenataNakonDodavanja = korisnikRepository.countByRole_Soba_Id(sobaId);
        if (brojPacijenataNakonDodavanja == soba.getKapacitet()) {
            soba.setStatus("Popunjena");
            sobaRepository.save(soba);
        }
    }

    @Transactional
    public void otpustiPacijenteIzSobe(Long sobaId, List<Long> pacijentIds) {
        Optional<Soba> sobaOptional = sobaRepository.findById(sobaId);
        if (sobaOptional.isEmpty()) {
            throw new RuntimeException("Soba nije pronađena");
        }

        Soba soba = sobaOptional.get();
        long trenutnoPacijenata = korisnikRepository.countByRole_Soba_Id(sobaId);

        if (trenutnoPacijenata < pacijentIds.size()) {
            throw new RuntimeException("Nema dovoljno pacijenata za otpustiti");
        }

        List<Korisnik> pacijenti = korisnikRepository.findAllById(pacijentIds);
        for (Korisnik pacijent : pacijenti) {
            if (pacijent.getRole() != null && pacijent.getRole().getSoba() != null &&
                    pacijent.getRole().getSoba().getId().equals(sobaId)) {
                pacijent.getRole().setSoba(null);
            } else {
                throw new RuntimeException("Pacijent nije u toj sobi");
            }
        }
        korisnikRepository.saveAll(pacijenti);

        // Update status ako više nije puna
        long preostaliPacijenti = korisnikRepository.countByRole_Soba_Id(sobaId);
        if (preostaliPacijenti < soba.getKapacitet()) {
            soba.setStatus("Dostupna");
            sobaRepository.save(soba);
        }
    }
}
