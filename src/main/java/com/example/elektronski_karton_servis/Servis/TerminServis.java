package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Termin;
import com.example.elektronski_karton_servis.Repository.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TerminServis {

    @Autowired
    TerminRepository terminRepository;

    public List<Termin> getAllTermini() {
        return terminRepository.findAll();
    }

    public Optional<Termin> findById(Integer terminUuid) {
        return terminRepository.findById(terminUuid);
    }

    public Termin saveTermin(Termin termin) {
        return terminRepository.save(termin);
    }

    public void deleteById(Integer terminUuid) {
        terminRepository.deleteById(terminUuid);
    }
}





