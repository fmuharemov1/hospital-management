package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Repository.TerapijaRepository;
import com.example.elektronski_karton_servis.Servis.TerapijaServis;
import com.example.elektronski_karton_servis.model.Terapija;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TerapijaServisTest {

    private TerapijaRepository repo;
    private TerapijaServis servis;

    @BeforeEach
    void init() {
        repo = mock(TerapijaRepository.class);
        servis = new TerapijaServis();
        servis = spy(servis);
        servis.terapijaRepository = repo;
    }

    private Terapija sample() {
        Terapija t = new Terapija();
        t.setId(1);
        t.setNaziv("Vitamini");
        t.setOpis("Jednom dnevno");
      t.setDatumPocetka(LocalDate.now().atStartOfDay());
       t.setDatumZavrsetka(LocalDate.now().plusDays(5).atStartOfDay());
       t.setOsobljeUuid((1));
        return t;
    }

    @Test
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, servis.getAllTerapije().size());
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
        Terapija t = sample();
        when(repo.save(t)).thenReturn(t);
        assertEquals(t, servis.saveTerapija(t));
    }

    @Test
    void delete() {
        servis.deleteById(1);
        verify(repo, times(1)).deleteById(1);
    }
}
