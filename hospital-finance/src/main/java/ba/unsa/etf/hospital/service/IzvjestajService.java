package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.repository.IzvjestajRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IzvjestajService {
    private final IzvjestajRepository izvjestajRepository;
    public IzvjestajService(IzvjestajRepository izvjestajRepository) {
        this.izvjestajRepository = izvjestajRepository;
    }
    public List<Izvjestaj> getAllIzvjestaji(){
        return izvjestajRepository.findAll();
    }
    public Izvjestaj saveIzvjestaj(Izvjestaj izvjestaj) {
        return izvjestajRepository.save(izvjestaj);
    }

    public Optional<Izvjestaj> findById(Long id) {
        return izvjestajRepository.findById(id);
    }

    public void deleteById(Long id) {
        izvjestajRepository.deleteById(id);
    }
}
