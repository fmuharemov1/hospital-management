package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.example.elektronski_karton_servis.Repository.DijagnozaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elektronski_karton_servis.Exception.DijagnozaNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DijagnozaServis {

    private final DijagnozaRepository dijagnozaRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator; // Deklarirajte validator

    @Autowired
    public DijagnozaServis(DijagnozaRepository dijagnozaRepository, ObjectMapper objectMapper, Validator validator) {
        this.dijagnozaRepository = dijagnozaRepository;
        this.objectMapper = objectMapper;
        this.validator = validator; // Inicijalizirajte validator
    }

    public List<Dijagnoza> getAllDijagnoze() {
        return dijagnozaRepository.findAll();
    }

    public Optional<Dijagnoza> findById(Integer id) {
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

    public void deleteById(Integer id) {
        dijagnozaRepository.deleteById(id);
    }
}
