package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.example.elektronski_karton_servis.Repository.DijagnozaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DijagnozaServis {

    @Autowired
    private DijagnozaRepository dijagnozaRepository;



    public List<Dijagnoza> getAllDijagnoze() {
        return dijagnozaRepository.findAll();
    }

    public Optional<Dijagnoza> findById(Integer id) {
        return dijagnozaRepository.findById(id);
    }

    public Dijagnoza saveDijagnoza(Dijagnoza dijagnoza) {
        return dijagnozaRepository.save(dijagnoza);
    }

    public void deleteById(Integer id) {
        dijagnozaRepository.deleteById(id);
    }
}


