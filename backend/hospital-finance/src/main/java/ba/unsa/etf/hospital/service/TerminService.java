package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.repository.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TerminService {
    @Autowired
    private TerminRepository terminRepository;

    @Autowired
    private FakturaService fakturaService;

    public TerminService(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
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
        Termin termin = terminRepository.findById(id)
                .orElseThrow(() -> new TerminNotFoundException(id));  // Ako nije pronađen, baci exception

        terminRepository.deleteById(id);
    }
    public Page<Termin> getSortedAndPaginatedTermini(int page, int size) {
        // Kreiramo Pageable objekat sa paginacijom i sortiranje po datumu (rastuce)
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("datumVrijeme")));
        return terminRepository.findAll(pageRequest);  // Koristimo findAll sa PageRequest objektom
    }
    public Page<Termin> getSortedAndPaginatedTerminiDesc(int page, int size) {
        // Kreiramo Pageable objekat sa paginacijom i sortiranje po datumu (opadajuće)
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("datumVrijeme")));
        return terminRepository.findAll(pageRequest);  // Koristimo findAll sa PageRequest objektom
    }
    @Transactional
    public Termin createTerminWithNewFaktura(Termin termin) {
        // Kreiraj novu Fakturu
        Faktura novaFaktura = new Faktura();
        novaFaktura.setIznos(100.00); // Dodaj logiku za iznos
        novaFaktura.setStatus("neplaćeno"); // Status fakture
        novaFaktura.setMetod("Gotovina"); // Metod plačanja

        // Spremi Fakturu u bazu
        Faktura savedFaktura = fakturaService.saveFaktura(novaFaktura);

        // Postavi samo ID fakture u Termin
        termin.setFaktura(savedFaktura); // Ovo je postavljanje cijele Faktura entiteta

        // Spremi Termin sa povezanim ID fakture
        Termin savedTermin = terminRepository.save(termin);

        // Vratiti sačuvani Termin
        return savedTermin;
    }

}
