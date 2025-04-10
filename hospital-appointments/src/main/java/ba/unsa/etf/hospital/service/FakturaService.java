package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.repository.FakturaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FakturaService {
    private final FakturaRepository fakturaRepository;
    public FakturaService(FakturaRepository fakturaRepository) {this.fakturaRepository = fakturaRepository;}
    public List<Faktura> getAllFakture() { return fakturaRepository.findAll(); }
    public Faktura saveFaktura(Faktura faktura) {
        return fakturaRepository.save(faktura);
    }

    public Optional<Faktura> findById(Long id) {
        return fakturaRepository.findById(id);
    }

    public void deleteById(Long id) {
        fakturaRepository.deleteById(id);
    }
}
