package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.repository.TerminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TerminServiceTest {

    @Mock
    private TerminRepository terminRepository;

    @InjectMocks
    private TerminService terminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTermini() {
        Termin termin1 = new Termin();
        termin1.setId(1L);
        termin1.setTerminUuid(UUID.randomUUID());
        termin1.setDatumVrijeme(LocalDateTime.now());

        Termin termin2 = new Termin();
        termin2.setId(2L);
        termin2.setTerminUuid(UUID.randomUUID());
        termin2.setDatumVrijeme(LocalDateTime.now());

        when(terminRepository.findAll()).thenReturn(Arrays.asList(termin1, termin2));

        // Testiranje metode
        var termini = terminService.getAllTermini();

        assertNotNull(termini);
        assertEquals(2, termini.size());
        assertEquals(termin1.getId(), termini.get(0).getId());
        assertEquals(termin2.getId(), termini.get(1).getId());

        verify(terminRepository, times(1)).findAll();
    }

    @Test
    public void testSaveTermin() {
        Termin termin = new Termin();
        termin.setTerminUuid(UUID.randomUUID());
        termin.setDatumVrijeme(LocalDateTime.now());

        when(terminRepository.save(any(Termin.class))).thenReturn(termin);

        // Testiranje metode
        Termin savedTermin = terminService.saveTermin(termin);

        assertNotNull(savedTermin);
        assertEquals(termin.getTerminUuid(), savedTermin.getTerminUuid());

        verify(terminRepository, times(1)).save(any(Termin.class));
    }

    @Test
    public void testFindById() {
        Termin termin = new Termin();
        termin.setId(1L);
        termin.setTerminUuid(UUID.randomUUID());
        termin.setDatumVrijeme(LocalDateTime.now());

        when(terminRepository.findById(1L)).thenReturn(Optional.of(termin));

        // Testiranje metode
        Optional<Termin> foundTermin = terminService.findById(1L);

        assertTrue(foundTermin.isPresent());
        assertEquals(termin.getId(), foundTermin.get().getId());

        verify(terminRepository, times(1)).findById(1L);
    }
    @Test
    public void testDeleteById_Termin() {
        Long id = 1L;

        Termin termin = new Termin();
        termin.setId(id);

        when(terminRepository.findById(id)).thenReturn(Optional.of(termin));
        doNothing().when(terminRepository).deleteById(id);

        terminService.deleteById(id);

        verify(terminRepository, times(1)).findById(id);
        verify(terminRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteById_Termin_NotFound() {
        Long id = 1L;

        when(terminRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TerminNotFoundException.class, () -> terminService.deleteById(id));

        verify(terminRepository, times(1)).findById(id);
        verify(terminRepository, never()).deleteById(id);
    }


}
