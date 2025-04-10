package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.repository.TerminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TerminServiceTest {

    @Mock
    private TerminRepository terminRepository;

    @InjectMocks
    private TerminService terminService;

    private Termin termin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy Termin object
        termin = new Termin();
        termin.setId(1L);
        termin.setPacijent(null);  // assuming you set the pacijent appropriately
        termin.setOsoblje(null);   // assuming you set the osoblje appropriately
        termin.setObavijest(null); // assuming you set the obavijest appropriately
        termin.setStatus("Active");
        termin.setDatumVrijeme(null);  // Assuming you will set an appropriate LocalDateTime
        termin.setTrajanje(30);
        termin.setMeet_link("meet.link");
    }

    @Test
    void testGetAllTermini() {
        // Arrange
        when(terminRepository.findAll()).thenReturn(Arrays.asList(termin));

        // Act
        var termini = terminService.getAllTermini();

        // Assert
        assertNotNull(termini);
        assertEquals(1, termini.size());
        assertEquals(termin.getStatus(), termini.get(0).getStatus());
    }

    @Test
    void testSaveTermin() {
        // Arrange
        when(terminRepository.save(termin)).thenReturn(termin);

        // Act
        Termin savedTermin = terminService.saveTermin(termin);

        // Assert
        assertNotNull(savedTermin);
        assertEquals(termin.getStatus(), savedTermin.getStatus());
        verify(terminRepository, times(1)).save(termin);
    }

    @Test
    void testFindById() {
        // Arrange
        when(terminRepository.findById(1L)).thenReturn(Optional.of(termin));

        // Act
        Optional<Termin> foundTermin = terminService.findById(1L);

        // Assert
        assertTrue(foundTermin.isPresent());
        assertEquals(termin.getStatus(), foundTermin.get().getStatus());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(terminRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Termin> foundTermin = terminService.findById(1L);

        // Assert
        assertFalse(foundTermin.isPresent());
    }

    @Test
    void testDeleteById() {
        // Act
        terminService.deleteById(1L);

        // Assert
        verify(terminRepository, times(1)).deleteById(1L);
    }
}
