package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KartonServis {

    private final KartonRepository kartonRepository;

    @Autowired
    public KartonServis(KartonRepository kartonRepository) {
        this.kartonRepository = kartonRepository;
    }

    public List<Karton> getAllKartoni() {
        return kartonRepository.findAll();
    }

    public Optional<Karton> findById(Integer id) {
        return kartonRepository.findById(id);
    }

    public Karton saveKarton(Karton karton) {
        return kartonRepository.save(karton);
    }

    public Optional<Karton> updateKarton(Integer id, Karton noviKarton) {
        return kartonRepository.findById(id)
                .map(existingKarton -> {
                    existingKarton.setPacijentUuid(noviKarton.getPacijentUuid());
                    existingKarton.setDatumKreiranja(noviKarton.getDatumKreiranja());
                    existingKarton.setBrojKartona(noviKarton.getBrojKartona());
                    return kartonRepository.save(existingKarton);
                });
    }

    public boolean deleteById(Integer id) {
        if (kartonRepository.existsById(id)) {
            kartonRepository.deleteById(id);
            return true;
        }
        return false;
    }
}