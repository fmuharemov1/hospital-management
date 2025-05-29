package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Repository.KartonRepository;
import com.example.elektronski_karton_servis.Servis.KartonServis;
import com.example.elektronski_karton_servis.model.Karton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KartonServisTest {

    private KartonRepository repo;
    private KartonServis servis;

    @BeforeEach
    void init() {
        repo = mock(KartonRepository.class);
        servis = new KartonServis(repo);
    }

    private Karton sample() {
        Karton k = new Karton();
        k.setId(1);
        k.setBrojKartona("BK001");
        k.setDatumKreiranja(LocalDate.now().atStartOfDay());
        k.setPacijentUuid((1));
        return k;
    }

    @Test
    void all() {
        when(repo.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, servis.getAllKartoni().size());
    }

    @Test
    void by_id_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample()));
        assertTrue(servis.findById(1).isPresent());
    }

    @Test
    void by_id_not_found() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertTrue(servis.findById(1).isEmpty());
    }

    @Test
    void save() {
        Karton k = sample();
        when(repo.save(k)).thenReturn(k);
        assertEquals(k, servis.saveKarton(k));
    }

    @Test
    void update_found() {
        Karton novi = sample();
        Karton stari = sample();
        when(repo.findById(1)).thenReturn(Optional.of(stari));
        when(repo.save(any())).thenReturn(novi);

        Optional<Karton> rezultat = servis.updateKarton(1, novi);
        assertTrue(rezultat.isPresent());
    }

    @Test
    void update_not_found() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertTrue(servis.updateKarton(1, sample()).isEmpty());
    }

    @Test
    void delete_found() {
        when(repo.existsById(1)).thenReturn(true);
        boolean obrisano = servis.deleteById(1);
        assertTrue(obrisano);
        verify(repo, times(1)).deleteById(1);
    }

    @Test
    void delete_not_found() {
        when(repo.existsById(1)).thenReturn(false);
        boolean obrisano = servis.deleteById(1);
        assertFalse(obrisano);
        verify(repo, never()).deleteById(1);
    }
}