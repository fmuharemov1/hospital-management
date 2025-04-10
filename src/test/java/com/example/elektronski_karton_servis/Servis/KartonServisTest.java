package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class KartonServisTest {

    @Mock
    private KartonRepository kartonRepository;

    @InjectMocks
    private KartonServis kartonServis;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllKartoni() {
        Karton karton1 = new Karton();
        karton1.setId(1);
        karton1.setPacijentUuid(123); // Koristite Integer
        karton1.setDatumKreiranja(LocalDateTime.now());
        karton1.setBrojKartona("K123");

        Karton karton2 = new Karton();
        karton2.setId(2);
        karton2.setPacijentUuid(456); // Koristite Integer
        karton2.setDatumKreiranja(LocalDateTime.now().minusDays(1));
        karton2.setBrojKartona("K456");

        when(kartonRepository.findAll()).thenReturn(Arrays.asList(karton1, karton2));

        var kartoni = kartonServis.getAllKartoni();

        assertNotNull(kartoni);
        assertEquals(2, kartoni.size());
        assertEquals(karton1.getId(), kartoni.get(0).getId());
        assertEquals(karton1.getPacijentUuid(), kartoni.get(0).getPacijentUuid());
        assertEquals(karton1.getBrojKartona(), kartoni.get(0).getBrojKartona()); // Dodano

        assertEquals(karton2.getId(), kartoni.get(1).getId());
        assertEquals(karton2.getPacijentUuid(), kartoni.get(1).getPacijentUuid());
        assertEquals(karton2.getBrojKartona(), kartoni.get(1).getBrojKartona());

        verify(kartonRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Karton karton = new Karton();
        karton.setId(1);
        karton.setPacijentUuid(123); // Koristite Integer
        karton.setDatumKreiranja(LocalDateTime.now());
        karton.setBrojKartona("K123");

        when(kartonRepository.findById(1)).thenReturn(Optional.of(karton));

        Optional<Karton> foundKarton = kartonServis.findById(1);

        assertTrue(foundKarton.isPresent());
        assertEquals(karton.getId(), foundKarton.get().getId());
        assertEquals(karton.getPacijentUuid(), foundKarton.get().getPacijentUuid());
        assertEquals(karton.getBrojKartona(), foundKarton.get().getBrojKartona());

        verify(kartonRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(kartonRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Karton> foundKarton = kartonServis.findById(1);

        assertFalse(foundKarton.isPresent());

        verify(kartonRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveKarton() {
        Karton kartonToSave = new Karton();
        kartonToSave.setPacijentUuid(123); // Koristite Integer
        kartonToSave.setDatumKreiranja(LocalDateTime.now());
        kartonToSave.setBrojKartona("K789");

        Karton savedKarton = new Karton();
        savedKarton.setId(1);
        savedKarton.setPacijentUuid(kartonToSave.getPacijentUuid());
        savedKarton.setDatumKreiranja(kartonToSave.getDatumKreiranja());
        savedKarton.setBrojKartona(kartonToSave.getBrojKartona());

        when(kartonRepository.save(any(Karton.class))).thenReturn(savedKarton);

        Karton result = kartonServis.saveKarton(kartonToSave);

        assertNotNull(result);
        assertEquals(savedKarton.getId(), result.getId());
        assertEquals(kartonToSave.getPacijentUuid(), result.getPacijentUuid());
        assertEquals(kartonToSave.getBrojKartona(), result.getBrojKartona());

        verify(kartonRepository, times(1)).save(any(Karton.class));
    }

    @Test
    public void testDeleteById() {
        int kartonIdToDelete = 1;
        doNothing().when(kartonRepository).deleteById(kartonIdToDelete);

        kartonServis.deleteById(kartonIdToDelete);

        verify(kartonRepository, times(1)).deleteById(kartonIdToDelete);
    }
}