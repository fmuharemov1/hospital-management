package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        karton1.setPacijentUuid(123);
        karton1.setDatumKreiranja(LocalDateTime.now());
        karton1.setBrojKartona("K123");

        Karton karton2 = new Karton();
        karton2.setId(2);
        karton2.setPacijentUuid(456);
        karton2.setDatumKreiranja(LocalDateTime.now().minusDays(1));
        karton2.setBrojKartona("K456");

        when(kartonRepository.findAll()).thenReturn(Arrays.asList(karton1, karton2));

        var kartoni = kartonServis.getAllKartoni();

        assertNotNull(kartoni);
        assertEquals(2, kartoni.size());
        assertEquals(karton1.getId(), kartoni.get(0).getId());
        assertEquals(karton1.getPacijentUuid(), kartoni.get(0).getPacijentUuid());
        assertEquals(karton1.getBrojKartona(), kartoni.get(0).getBrojKartona());

        assertEquals(karton2.getId(), kartoni.get(1).getId());
        assertEquals(karton2.getPacijentUuid(), kartoni.get(1).getPacijentUuid());
        assertEquals(karton2.getBrojKartona(), kartoni.get(1).getBrojKartona());

        verify(kartonRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Karton karton = new Karton();
        karton.setId(1);
        karton.setPacijentUuid(123);
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
        kartonToSave.setPacijentUuid(123);
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
    public void testUpdateKarton() {
        Integer kartonIdToUpdate = 1;
        Karton existingKarton = new Karton();
        existingKarton.setId(kartonIdToUpdate);
        existingKarton.setPacijentUuid(111);
        existingKarton.setDatumKreiranja(LocalDateTime.now().minusDays(2));
        existingKarton.setBrojKartona("OLD123");

        Karton updatedKartonDetails = new Karton();
        updatedKartonDetails.setPacijentUuid(222);
        updatedKartonDetails.setDatumKreiranja(LocalDateTime.now());
        updatedKartonDetails.setBrojKartona("NEW456");

        Karton savedUpdatedKarton = new Karton();
        savedUpdatedKarton.setId(kartonIdToUpdate);
        savedUpdatedKarton.setPacijentUuid(updatedKartonDetails.getPacijentUuid());
        savedUpdatedKarton.setDatumKreiranja(updatedKartonDetails.getDatumKreiranja());
        savedUpdatedKarton.setBrojKartona(updatedKartonDetails.getBrojKartona());

        when(kartonRepository.findById(kartonIdToUpdate)).thenReturn(Optional.of(existingKarton));
        when(kartonRepository.save(any(Karton.class))).thenReturn(savedUpdatedKarton);

        Optional<Karton> result = kartonServis.updateKarton(kartonIdToUpdate, updatedKartonDetails);

        assertTrue(result.isPresent());
        assertEquals(kartonIdToUpdate, result.get().getId());
        assertEquals(updatedKartonDetails.getPacijentUuid(), result.get().getPacijentUuid());
        assertEquals(updatedKartonDetails.getBrojKartona(), result.get().getBrojKartona());

        verify(kartonRepository, times(1)).findById(kartonIdToUpdate);
        verify(kartonRepository, times(1)).save(existingKarton); // existingKarton se ažurira i sprema
    }

    @Test
    public void testUpdateKarton_NotFound() {
        Integer kartonIdToUpdate = 1;
        Karton updatedKartonDetails = new Karton();
        updatedKartonDetails.setPacijentUuid(222);
        updatedKartonDetails.setDatumKreiranja(LocalDateTime.now());
        updatedKartonDetails.setBrojKartona("NEW456");

        when(kartonRepository.findById(kartonIdToUpdate)).thenReturn(Optional.empty());

        Optional<Karton> result = kartonServis.updateKarton(kartonIdToUpdate, updatedKartonDetails);

        assertFalse(result.isPresent());

        verify(kartonRepository, times(1)).findById(kartonIdToUpdate);
        verify(kartonRepository, never()).save(any(Karton.class));
    }

    @Test
    public void testDeleteById() {
        int kartonIdToDelete = 1;

        // Postavite ponašanje za existsById da vrati true (karton postoji)
        when(kartonRepository.existsById(kartonIdToDelete)).thenReturn(true);

        // Postavite ponašanje za deleteById da ne radi ništa
        doNothing().when(kartonRepository).deleteById(kartonIdToDelete);

        boolean result = kartonServis.deleteById(kartonIdToDelete);

        assertTrue(result);
        verify(kartonRepository, times(1)).existsById(kartonIdToDelete);
        verify(kartonRepository, times(1)).deleteById(kartonIdToDelete);
    }

    @Test
    public void testDeleteById_NotFound() {
        int kartonIdToDelete = 1;

        // Postavite ponašanje za existsById da vrati false (karton ne postoji)
        when(kartonRepository.existsById(kartonIdToDelete)).thenReturn(false);

        boolean result = kartonServis.deleteById(kartonIdToDelete);

        assertFalse(result);
        verify(kartonRepository, times(1)).existsById(kartonIdToDelete);
        verify(kartonRepository, never()).deleteById(kartonIdToDelete);
    }
}