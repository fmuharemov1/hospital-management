package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.repository.SobaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SobaService {
    private final SobaRepository sobaRepository;

    public SobaService(SobaRepository sobaRepository) {
        this.sobaRepository = sobaRepository;
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
        sobaRepository.deleteById(id);
    }
}