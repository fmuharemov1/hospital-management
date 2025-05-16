package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.client.GrpcSystemEventsClient;
import com.example.elektronski_karton_servis.model.Termin;
import com.example.elektronski_karton_servis.Repository.TerminRepository;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerminServis {

    @Autowired
    @Lazy
    private GrpcSystemEventsClient grpcClient;


    @Autowired
    public TerminRepository terminRepository;


    public List<Termin> getAllTermini() {
        System.out.println("✅ POZVAN getAllTermini()");
        grpcClient.log("get", "elektronski-karton-servis", "termin");
        return terminRepository.findAll();
    }


    public Optional<Termin> findById(Integer terminUuid) {
        Optional<Termin> termin = terminRepository.findById(terminUuid);

        if (termin.isEmpty()) {
            grpcClient.log("get", "elektronski-karton-servis", "termin (NOT FOUND)");
        } else {
            grpcClient.log("get", "elektronski-karton-servis", "termin");
        }

        return termin;
    }


    public Termin saveTermin(Termin termin) {
        grpcClient.log("create", "elektronski-karton-servis", "termin");
        return terminRepository.save(termin);
    }

    public void deleteById(Integer terminUuid) {
        grpcClient.log("delete", "elektronski-karton-servis", "termin");
        terminRepository.deleteById(terminUuid);
    }
}
