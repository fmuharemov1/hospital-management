package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Repository.DijagnozaRepository;
import com.example.elektronski_karton_servis.client.TerminClient;
import com.example.elektronski_karton_servis.dto.TerminDTO;
import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DijagnozaServis {

    private final DijagnozaRepository dijagnozaRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final TerminClient terminClient; // obavezno inicijalizirati

    @Autowired
    public DijagnozaServis(DijagnozaRepository dijagnozaRepository,
                           ObjectMapper objectMapper,
                           Validator validator,
                           TerminClient terminClient) { // dodaj ovdje
        this.dijagnozaRepository = dijagnozaRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.terminClient = terminClient; // inicijalizacija
    }

    public List<Dijagnoza> getAllDijagnoze() {
        return dijagnozaRepository.findAll();
    }

    public java.util.Optional<Dijagnoza> findById(Integer id) {
        return dijagnozaRepository.findById(id);
    }

    public Dijagnoza saveDijagnoza(Dijagnoza dijagnoza) {
        return dijagnozaRepository.save(dijagnoza);
    }

    public Optional<Dijagnoza> updateDijagnoza(Integer id, Dijagnoza novaDijagnoza) {
        return dijagnozaRepository.findById(id)
                .map(postojećaDijagnoza -> {
                    postojećaDijagnoza.setKartonId(novaDijagnoza.getKartonId());
                    postojećaDijagnoza.setOsobljeUuid(novaDijagnoza.getOsobljeUuid());
                    postojećaDijagnoza.setNaziv(novaDijagnoza.getNaziv());
                    postojećaDijagnoza.setOpis(novaDijagnoza.getOpis());
                    postojećaDijagnoza.setDatumDijagnoze(novaDijagnoza.getDatumDijagnoze());
                    return dijagnozaRepository.save(postojećaDijagnoza);
                });
    }

    public TerminDTO getTerminById(Long id) {
        return terminClient.getTermin(id);
    }

    public void deleteById(Integer id) {
        dijagnozaRepository.deleteById(id);
    }
}
