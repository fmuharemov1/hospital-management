package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KartonServis {

    @Autowired
    private KartonRepository kartonRepository;

    public List<Karton> getAllKartoni() {
        return kartonRepository.findAll();
    }

    public Optional<Karton> findById(Integer id) {
        return kartonRepository.findById(id);
    }

    public Karton saveKarton(Karton karton) {
        return kartonRepository.save(karton);
    }

    public void deleteById(Integer id) {
        kartonRepository.deleteById(id);
    }
}



