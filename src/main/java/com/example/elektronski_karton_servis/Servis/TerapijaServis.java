package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Terapija;
import com.example.elektronski_karton_servis.Repository.TerapijaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerapijaServis {

    @Autowired
    TerapijaRepository terapijaRepository;

    public List<Terapija> getAllTerapije() {
        return terapijaRepository.findAll();
    }

    public Optional<Terapija> findById(Integer id) {
        return terapijaRepository.findById(id);
    }

    public Terapija saveTerapija(Terapija terapija) {
        return terapijaRepository.save(terapija);
    }

    public void deleteById(Integer id) {
        terapijaRepository.deleteById(id);
    }
}



