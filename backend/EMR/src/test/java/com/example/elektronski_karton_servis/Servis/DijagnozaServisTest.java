package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Repository.DijagnozaRepository;
import com.example.elektronski_karton_servis.client.TerminClient;
import com.example.elektronski_karton_servis.dto.TerminDTO;
import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DijagnozaServisTest {

    private DijagnozaRepository repo;
    private TerminClient terminClient;
    private DijagnozaServis servis;

    @BeforeEach
    void init() {
        repo = mock(DijagnozaRepository.class);
        terminClient = mock(TerminClient.class); // dodaj ovo
        ObjectMapper mapper = new ObjectMapper();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        servis = new DijagnozaServis(repo, mapper, validator, terminClient); // sada odgovara konstruktoru
    }

    private Dijagnoza sample() {
        Dijagnoza d = new Dijagnoza();
        d.setId(1);
        d.setNaziv("Gripa");
        d.setOpis("Opis");
        d.setKartonId(12);
        d.setDatumDijagnoze(LocalDateTime.now());
        d.setOsobljeUuid(1);
        return d;
    }

    @Test
    void all() {
        when(repo.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, servis.getAllDijagnoze().size());
    }

    @Test
    void findById_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample()));
        assertTrue(servis.findById(1).isPresent());
    }

    @Test
    void findById_notFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertTrue(servis.findById(1).isEmpty());
    }

    @Test
    void save() {
        Dijagnoza d = sample();
        when(repo.save(d)).thenReturn(d);
        assertEquals(d, servis.saveDijagnoza(d));
    }

    @Test
    void update_found() {
        Dijagnoza novi = sample();
        when(repo.findById(1)).thenReturn(Optional.of(sample()));
        when(repo.save(any())).thenReturn(novi);
        assertTrue(servis.updateDijagnoza(1, novi).isPresent());
    }

    @Test
    void update_notFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertTrue(servis.updateDijagnoza(1, sample()).isEmpty());
    }

    @Test
    void delete() {
        servis.deleteById(1);
        verify(repo, times(1)).deleteById(1);
    }
    @Test
    void getTerminById_returnsTermin() {
        TerminDTO termin = new TerminDTO();
        termin.setId(5L);
        termin.setVrijemePocetka("10:00");
        termin.setVrijemeKraja("10:30");

        when(terminClient.getTermin(5L)).thenReturn(termin);

        TerminDTO rezultat = servis.getTerminById(5L);

        assertNotNull(rezultat);
        assertEquals("10:00", rezultat.getVrijemePocetka());
        assertEquals("10:30", rezultat.getVrijemeKraja());
        verify(terminClient, times(1)).getTermin(5L);
    }

}
