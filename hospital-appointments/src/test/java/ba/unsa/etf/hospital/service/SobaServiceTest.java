package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.repository.SobaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SobaServiceTest {

    @Mock
    private SobaRepository sobaRepository;

    @InjectMocks
    private SobaService sobaService;

    private Soba soba;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy Soba object
        soba = new Soba();
        soba.setId(1L);
        soba.setBroj_sobe("101");
        soba.setStatus("Occupied");
        soba.setKapacitet(2);
        soba.setKorisnik(null);  // assuming you set the korisnik appropriately
    }

    @Test
    void testGetAllSobe() {
        // Arrange
        when(sobaRepository.findAll()).thenReturn(Arrays.asList(soba));

        // Act
        var sobe = sobaService.getAllSobe();

        // Assert
        assertNotNull(sobe);
        assertEquals(1, sobe.size());
        assertEquals(soba.getStatus(), sobe.get(0).getStatus());
    }

    @Test
    void testSaveSoba() {
        // Arrange
        when(sobaRepository.save(soba)).thenReturn(soba);

        // Act
        Soba savedSoba = sobaService.saveSoba(soba);

        // Assert
        assertNotNull(savedSoba);
        assertEquals(soba.getStatus(), savedSoba.getStatus());
        verify(sobaRepository, times(1)).save(soba);
    }

    @Test
    void testFindById() {
        // Arrange
        when(sobaRepository.findById(1L)).thenReturn(Optional.of(soba));

        // Act
        Optional<Soba> foundSoba = sobaService.findById(1L);

        // Assert
        assertTrue(foundSoba.isPresent());
        assertEquals(soba.getStatus(), foundSoba.get().getStatus());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(sobaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Soba> foundSoba = sobaService.findById(1L);

        // Assert
        assertFalse(foundSoba.isPresent());
    }

    @Test
    void testDeleteById() {
        // Act
        sobaService.deleteById(1L);

        // Assert
        verify(sobaRepository, times(1)).deleteById(1L);
    }
}
