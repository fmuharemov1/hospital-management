package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.repository.IzvjestajRepository;

import java.util.List;

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

}
