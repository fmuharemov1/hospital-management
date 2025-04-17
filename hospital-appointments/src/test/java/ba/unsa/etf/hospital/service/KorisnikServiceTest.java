package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class KorisnikServiceTest {

    @Mock
    private KorisnikRepository korisnikRepository;

    @InjectMocks
    private KorisnikService korisnikService;

    private Korisnik korisnik;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy Korisnik object
        korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setKorisnikUuid(UUID.randomUUID());
        korisnik.setIme("John");
        korisnik.setPrezime("Doe");
        korisnik.setEmail("john.doe@example.com");
        korisnik.setLozinka("password123");
        korisnik.setBr_telefona("+123456789");
    }

    @Test
    void testGetAllKorisnici() {
        // Arrange
        when(korisnikRepository.findAll()).thenReturn(Arrays.asList(korisnik));

        // Act
        var korisnici = korisnikService.getAllKorisnici();

        // Assert
        assertNotNull(korisnici);
        assertEquals(1, korisnici.size());
        assertEquals(korisnik.getIme(), korisnici.get(0).getIme());
    }

    @Test
    void testSaveKorisnik() {
        // Arrange
        when(korisnikRepository.save(korisnik)).thenReturn(korisnik);

        // Act
        Korisnik savedKorisnik = korisnikService.saveKorisnik(korisnik);

        // Assert
        assertNotNull(savedKorisnik);
        assertEquals(korisnik.getIme(), savedKorisnik.getIme());
        verify(korisnikRepository, times(1)).save(korisnik);
    }

    @Test
    void testFindById() {
        // Arrange
        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(korisnik));

        // Act
        Optional<Korisnik> foundKorisnik = korisnikService.findById(1L);

        // Assert
        assertTrue(foundKorisnik.isPresent());
        assertEquals(korisnik.getIme(), foundKorisnik.get().getIme());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(korisnikRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Korisnik> foundKorisnik = korisnikService.findById(1L);

        // Assert
        assertFalse(foundKorisnik.isPresent());
    }

    @Test
    void testDeleteById() {
        // Arrange
        when(korisnikRepository.existsById(1L)).thenReturn(true);

        // Act
        korisnikService.deleteById(1L);

        // Assert
        verify(korisnikRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdThrowsExceptionWhenKorisnikNotFound() {
        // Arrange
        when(korisnikRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(KorisnikNotFoundException.class, () -> korisnikService.deleteById(1L));
    }
}