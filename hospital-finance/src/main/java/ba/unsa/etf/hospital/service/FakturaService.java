package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.repository.FakturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FakturaService {
    private final FakturaRepository fakturaRepository;

    public FakturaService(FakturaRepository fakturaRepository) {
        this.fakturaRepository = fakturaRepository;
    }

    public List<Faktura> getAllFakture() {
        return fakturaRepository.findAll();
    }
    @Transactional
    public List<Faktura> saveBatchFakture(List<Faktura> fakture) {
        List<Faktura> saved = new ArrayList<>();
        for (Faktura f : fakture) {
            // neka custom validacija ili obrada prije spremanja
            if (f.getIznos() > 0) {
                f.setStatus("neplaćeno");
                saved.add(fakturaRepository.save(f));
            }
        }
        return saved;
    }
    public Faktura saveFaktura(Faktura faktura) {
        return fakturaRepository.save(faktura);
    }

    public Optional<Faktura> findById(Long id) {
        return fakturaRepository.findById(id);
    }

    public void deleteById(Long id) {
        fakturaRepository.findById(id)
                .orElseThrow(() -> new FakturaNotFoundException(id));
        fakturaRepository.deleteById(id);
    }

}
