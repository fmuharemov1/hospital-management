package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.ObavijestNotFoundException;
import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.repository.ObavijestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObavijestService {
    private final ObavijestRepository obavijestRepository;

    public ObavijestService(ObavijestRepository obavijestRepository) {
        this.obavijestRepository = obavijestRepository;
    }

    public List<Obavijest> getAllObavijesti() {
        return obavijestRepository.findAll();
    }

    public Obavijest saveObavijest(Obavijest obavijest) {
        return obavijestRepository.save(obavijest);
    }

    public Optional<Obavijest> findById(Long id) {
        return obavijestRepository.findById(id);
    }

    public void deleteById(Long id) {
        if (!obavijestRepository.existsById(id)) {
            throw new ObavijestNotFoundException(id);
        }
        obavijestRepository.deleteById(id);
    }
}
