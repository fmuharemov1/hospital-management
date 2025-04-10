package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Termin;
import com.example.elektronski_karton_servis.Repository.TerminRepository;
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

public class TerminServisTest {

    @Mock
    private TerminRepository terminRepository;

    @InjectMocks
    private TerminServis terminServis;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTermini() {
        Termin termin1 = new Termin();
        termin1.setId(1);
        termin1.setPacijentUuid(123);
        termin1.setOsobljeUuid(456);
        termin1.setDatumVrijeme(LocalDateTime.now());
        termin1.setTermintUuid(789);

        Termin termin2 = new Termin();
        termin2.setId(2);
        termin2.setPacijentUuid(111);
        termin2.setOsobljeUuid(222);
        termin2.setDatumVrijeme(LocalDateTime.now().plusHours(1));
        termin2.setTermintUuid(333);

        when(terminRepository.findAll()).thenReturn(Arrays.asList(termin1, termin2));

        // Testiranje metode
        var termini = terminServis.getAllTermini();

        assertNotNull(termini);
        assertEquals(2, termini.size());
        assertEquals(termin1.getId(), termini.get(0).getId());
        assertEquals(termin1.getPacijentUuid(), termini.get(0).getPacijentUuid());
        assertEquals(termin1.getOsobljeUuid(), termini.get(0).getOsobljeUuid());
        assertEquals(termin1.getDatumVrijeme(), termini.get(0).getDatumVrijeme());
        assertEquals(termin1.getTermintUuid(), termini.get(0).getTermintUuid());

        assertEquals(termin2.getId(), termini.get(1).getId());
        assertEquals(termin2.getPacijentUuid(), termini.get(1).getPacijentUuid());
        assertEquals(termin2.getOsobljeUuid(), termini.get(1).getOsobljeUuid());
        assertEquals(termin2.getDatumVrijeme(), termini.get(1).getDatumVrijeme());
        assertEquals(termin2.getTermintUuid(), termini.get(1).getTermintUuid());

        verify(terminRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        int terminIdToFind = 1;
        Termin termin = new Termin();
        termin.setId(terminIdToFind);
        termin.setPacijentUuid(123);
        termin.setOsobljeUuid(456);
        termin.setDatumVrijeme(LocalDateTime.now());
        termin.setTermintUuid(789);

        when(terminRepository.findById(terminIdToFind)).thenReturn(Optional.of(termin));

        // Testiranje metode
        Optional<Termin> foundTermin = terminServis.findById(terminIdToFind);

        assertTrue(foundTermin.isPresent());
        assertEquals(termin.getId(), foundTermin.get().getId());
        assertEquals(termin.getPacijentUuid(), foundTermin.get().getPacijentUuid());
        assertEquals(termin.getOsobljeUuid(), foundTermin.get().getOsobljeUuid());
        assertEquals(termin.getDatumVrijeme(), foundTermin.get().getDatumVrijeme());
        assertEquals(termin.getTermintUuid(), foundTermin.get().getTermintUuid());

        verify(terminRepository, times(1)).findById(terminIdToFind);
    }

    @Test
    public void testFindById_NotFound() {
        int terminIdToFind = 1;
        when(terminRepository.findById(terminIdToFind)).thenReturn(Optional.empty());

        // Testiranje metode
        Optional<Termin> foundTermin = terminServis.findById(terminIdToFind);

        assertFalse(foundTermin.isPresent());

        verify(terminRepository, times(1)).findById(terminIdToFind);
    }

    @Test
    public void testSaveTermin() {
        Termin terminToSave = new Termin();
        terminToSave.setPacijentUuid(123);
        terminToSave.setOsobljeUuid(456);
        terminToSave.setDatumVrijeme(LocalDateTime.now().plusMinutes(30));
        terminToSave.setTermintUuid(789);

        Termin savedTermin = new Termin();
        savedTermin.setId(1);
        savedTermin.setPacijentUuid(terminToSave.getPacijentUuid());
        savedTermin.setOsobljeUuid(terminToSave.getOsobljeUuid());
        savedTermin.setDatumVrijeme(terminToSave.getDatumVrijeme());
        savedTermin.setTermintUuid(terminToSave.getTermintUuid());

        when(terminRepository.save(any(Termin.class))).thenReturn(savedTermin);

        // Testiranje metode
        Termin result = terminServis.saveTermin(terminToSave);

        assertNotNull(result);
        assertEquals(savedTermin.getId(), result.getId());
        assertEquals(terminToSave.getPacijentUuid(), result.getPacijentUuid());
        assertEquals(terminToSave.getOsobljeUuid(), result.getOsobljeUuid());
        assertEquals(terminToSave.getDatumVrijeme(), result.getDatumVrijeme());
        assertEquals(terminToSave.getTermintUuid(), result.getTermintUuid());

        verify(terminRepository, times(1)).save(any(Termin.class));
    }

    @Test
    public void testDeleteById() {
        int terminIdToDelete = 1;
        doNothing().when(terminRepository).deleteById(terminIdToDelete);

        // Testiranje metode
        terminServis.deleteById(terminIdToDelete);

        verify(terminRepository, times(1)).deleteById(terminIdToDelete);
    }
}