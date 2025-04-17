package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Repository.TerminRepository;
import com.example.elektronski_karton_servis.Servis.TerminServis;
import com.example.elektronski_karton_servis.model.Termin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TerminServisTest {

    private TerminRepository repo;
    private TerminServis servis;

    @BeforeEach
    void init() {
        repo = mock(TerminRepository.class);
        servis = new TerminServis();
        servis = spy(servis);
        servis.terminRepository = repo;
    }

    private Termin sample() {
        Termin t = new Termin();
        t.setId(1);
        t.setPacijentUuid((1));
        t.setOsobljeUuid((2));
        t.setDatumVrijeme(LocalDateTime.now().plusDays(1));
        return t;
    }

    @Test
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, servis.getAllTermini().size());
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
        Termin t = sample();
        when(repo.save(t)).thenReturn(t);
        assertEquals(t, servis.saveTermin(t));
    }

    @Test
    void delete() {
        servis.deleteById(1);
        verify(repo, times(1)).deleteById(1);
    }
}