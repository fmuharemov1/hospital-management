package com.example.elektronski_karton_servis.Servis;
import com.example.logging.GrpcSystemEventsClient;

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
        grpcClient.log("update", "emr", "termin", "SUCCESS", "ilhana");
        return terminRepository.findAll();
    }


    public Optional<Termin> findById(Integer terminUuid) {
        Optional<Termin> termin = terminRepository.findById(terminUuid);

        if (termin.isEmpty()) {
            grpcClient.log("update", "emr", "termin", "SUCCESS", "ilhana");

        } else {
            grpcClient.log("update", "emr", "termin", "SUCCESS", "ilhana");

        }

        return termin;
    }


    public Termin saveTermin(Termin termin) {
        grpcClient.log("update", "emr", "termin", "SUCCESS", "ilhana");

        return terminRepository.save(termin);
    }

    public void deleteById(Integer terminUuid) {
        grpcClient.log("update", "emr", "termin", "SUCCESS", "ilhana");

        terminRepository.deleteById(terminUuid);
    }
}
