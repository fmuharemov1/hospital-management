package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.repository.FakturaRepository;

import java.util.List;

public class FakturaService {
    private final FakturaRepository fakturaRepository;
    public FakturaService(FakturaRepository fakturaRepository) {this.fakturaRepository = fakturaRepository;}
    public List<Faktura> getAllFakture() { return fakturaRepository.findAll(); }
    public Faktura saveFaktura(Faktura faktura) {
        return fakturaRepository.save(faktura);
    }
}
