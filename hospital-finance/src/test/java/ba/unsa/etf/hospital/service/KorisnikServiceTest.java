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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KorisnikServiceTest {

    @Mock
    private KorisnikRepository korisnikRepository;

    @InjectMocks
    private KorisnikService korisnikService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllKorisnici() {
        Korisnik korisnik1 = new Korisnik();
        korisnik1.setId(1L);
        korisnik1.setKorisnikUuid(UUID.randomUUID());

        Korisnik korisnik2 = new Korisnik();
        korisnik2.setId(2L);
        korisnik2.setKorisnikUuid(UUID.randomUUID());

        when(korisnikRepository.findAll()).thenReturn(Arrays.asList(korisnik1, korisnik2));

        // Testiranje metode
        var korisnici = korisnikService.getAllKorisnici();

        assertNotNull(korisnici);
        assertEquals(2, korisnici.size());
        assertEquals(korisnik1.getId(), korisnici.get(0).getId());
        assertEquals(korisnik2.getId(), korisnici.get(1).getId());

        verify(korisnikRepository, times(1)).findAll();
    }

    @Test
    public void testSaveKorisnik() {
        Korisnik korisnik = new Korisnik();
        korisnik.setKorisnikUuid(UUID.randomUUID());

        when(korisnikRepository.save(any(Korisnik.class))).thenReturn(korisnik);

        // Testiranje metode
        Korisnik savedKorisnik = korisnikService.saveKorisnik(korisnik);

        assertNotNull(savedKorisnik);
        assertEquals(korisnik.getKorisnikUuid(), savedKorisnik.getKorisnikUuid());

        verify(korisnikRepository, times(1)).save(any(Korisnik.class));
    }

    @Test
    public void testFindById() {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setKorisnikUuid(UUID.randomUUID());

        when(korisnikRepository.findById(1L)).thenReturn(Optional.of(korisnik));

        // Testiranje metode
        Optional<Korisnik> foundKorisnik = korisnikService.findById(1L);

        assertTrue(foundKorisnik.isPresent());
        assertEquals(korisnik.getId(), foundKorisnik.get().getId());

        verify(korisnikRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(korisnikRepository.findById(1L)).thenReturn(Optional.empty());

        // Testiranje metode
        Optional<Korisnik> foundKorisnik = korisnikService.findById(1L);

        assertFalse(foundKorisnik.isPresent());

        verify(korisnikRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById_Korisnik() {
        Long id = 1L;

        Korisnik korisnik = new Korisnik();
        korisnik.setId(id);

        when(korisnikRepository.findById(id)).thenReturn(Optional.of(korisnik));
        doNothing().when(korisnikRepository).deleteById(id);

        korisnikService.deleteById(id);

        verify(korisnikRepository, times(1)).findById(id);
        verify(korisnikRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteById_Korisnik_NotFound() {
        Long id = 1L;

        when(korisnikRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(KorisnikNotFoundException.class, () -> korisnikService.deleteById(id));

        verify(korisnikRepository, times(1)).findById(id);
        verify(korisnikRepository, never()).deleteById(id);
    }

}
