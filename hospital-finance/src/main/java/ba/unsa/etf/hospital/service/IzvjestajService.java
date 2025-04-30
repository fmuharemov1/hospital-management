package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.IzvjestajNotFoundException;
import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.repository.IzvjestajRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Izvjestaj> getIzvjestajiSortedByFinansijskiPregled(int page, int size) {
        // Sortiraj po 'finansijskiPregled' u rastućem redoslijedu
        return izvjestajRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.asc("finansijskiPregled"))));
    }
    public Optional<Izvjestaj> findById(Long id) {
        return izvjestajRepository.findById(id);
    }

    public void deleteById(Long id) {
        Izvjestaj izvjestaj = izvjestajRepository.findById(id)
                .orElseThrow(() -> new IzvjestajNotFoundException(id));

        izvjestajRepository.deleteById(id);
    }
    public List<Izvjestaj> getIzvjestajiByTipAndMinPacijenti(String tipIzvjestaja, int minBrojPacijenata) {
        return izvjestajRepository.findIzvjestajiByTipAndPacijentiGreaterThan(tipIzvjestaja, minBrojPacijenata);
    }
    public List<Izvjestaj> getIzvjestajiByTipAndMinIznos(String tipIzvjestaja, double minIznos) {
        return izvjestajRepository.findIzvjestajiByTipAndIznosGreaterThan(tipIzvjestaja, minIznos);
    }
    @Transactional(rollbackFor = Exception.class)
    public List<Izvjestaj> saveBatchIzvjestaji(List<Izvjestaj> izvjestaji) {
        return izvjestajRepository.saveAll(izvjestaji);  // Unosi sve izvještaje u bazu
    }

    public List<Izvjestaj> getIzvjestajiByTip(String tipIzvjestaja) {
        return izvjestajRepository.findByTipIzvjestaja(tipIzvjestaja);
    }
}
