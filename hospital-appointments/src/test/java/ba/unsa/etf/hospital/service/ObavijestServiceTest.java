package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.repository.ObavijestRepository;
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

class ObavijestServiceTest {

    @Mock
    private ObavijestRepository obavijestRepository;

    @InjectMocks
    private ObavijestService obavijestService;

    private Obavijest obavijest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy Obavijest object with LocalDateTime for datum_vrijeme
        obavijest = new Obavijest();
        obavijest.setId(1L);
        obavijest.setSadrzaj("This is a test notification.");
        obavijest.setDatum_vrijeme(LocalDateTime.of(2025, 4, 10, 10, 30));  // Setting a date and time
    }

    @Test
    void testGetAllObavijesti() {
        // Arrange
        when(obavijestRepository.findAll()).thenReturn(Arrays.asList(obavijest));

        // Act
        var obavijesti = obavijestService.getAllObavijesti();

        // Assert
        assertNotNull(obavijesti);
        assertEquals(1, obavijesti.size());
        assertEquals(obavijest.getSadrzaj(), obavijesti.get(0).getSadrzaj());
    }

    @Test
    void testSaveObavijest() {
        // Arrange
        when(obavijestRepository.save(obavijest)).thenReturn(obavijest);

        // Act
        Obavijest savedObavijest = obavijestService.saveObavijest(obavijest);

        // Assert
        assertNotNull(savedObavijest);
        assertEquals(obavijest.getSadrzaj(), savedObavijest.getSadrzaj());
        assertEquals(obavijest.getDatum_vrijeme(), savedObavijest.getDatum_vrijeme());
        verify(obavijestRepository, times(1)).save(obavijest);
    }

    @Test
    void testFindById() {
        // Arrange
        when(obavijestRepository.findById(1L)).thenReturn(Optional.of(obavijest));

        // Act
        Optional<Obavijest> foundObavijest = obavijestService.findById(1L);

        // Assert
        assertTrue(foundObavijest.isPresent());
        assertEquals(obavijest.getSadrzaj(), foundObavijest.get().getSadrzaj());
        assertEquals(obavijest.getDatum_vrijeme(), foundObavijest.get().getDatum_vrijeme());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(obavijestRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Obavijest> foundObavijest = obavijestService.findById(1L);

        // Assert
        assertFalse(foundObavijest.isPresent());
    }

    @Test
    void testDeleteById() {
        // Act
        obavijestService.deleteById(1L);

        // Assert
        verify(obavijestRepository, times(1)).deleteById(1L);
    }
}
