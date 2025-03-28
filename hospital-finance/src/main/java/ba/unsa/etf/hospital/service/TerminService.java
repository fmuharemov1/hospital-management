package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.repository.TerminRepository;

import java.util.List;

public class TerminService {
    private final TerminRepository terminRepository;
    public TerminService(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }
    public List<Termin> getAllTermini(){
        return terminRepository.findAll();
    }
    public Termin saveTermin(Termin termin) {
        return terminRepository.save(termin);
    }
}
