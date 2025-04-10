package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Korisnik;
import com.example.elektronski_karton_servis.Repository.KorisnikRepository;
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

public class KorisnikServisTest {

    @Mock
    private KorisnikRepository korisnikRepository;

    @InjectMocks
    private KorisnikServis korisnikServis;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllKorisnici() {
        Korisnik korisnik1 = new Korisnik();
        korisnik1.setId(1); // Pretpostavljam da je ID Integer
        korisnik1.setKorisnikUuid(UUID.randomUUID());
        korisnik1.setIme("Ime1");
        korisnik1.setPrezime("Prezime1");

        Korisnik korisnik2 = new Korisnik();
        korisnik2.setId(2); // Pretpostavljam da je ID Integer
        korisnik2.setKorisnikUuid(UUID.randomUUID());
        korisnik2.setIme("Ime2");
        korisnik2.setPrezime("Prezime2");

        when(korisnikRepository.findAll()).thenReturn(Arrays.asList(korisnik1, korisnik2));

        // Testiranje metode
        var korisnici = korisnikServis.getAllKorisnici();

        assertNotNull(korisnici);
        assertEquals(2, korisnici.size());
        assertEquals(korisnik1.getId(), korisnici.get(0).getId());
        assertEquals(korisnik1.getIme(), korisnici.get(0).getIme());
        assertEquals(korisnik2.getId(), korisnici.get(1).getId());
        assertEquals(korisnik2.getPrezime(), korisnici.get(1).getPrezime());

        verify(korisnikRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1); // Pretpostavljam da je ID Integer
        korisnik.setKorisnikUuid(UUID.randomUUID());
        korisnik.setIme("TestIme");
        korisnik.setPrezime("TestPrezime");

        when(korisnikRepository.findById(1)).thenReturn(Optional.of(korisnik));

        // Testiranje metode
        Optional<Korisnik> foundKorisnik = korisnikServis.findById(1);

        assertTrue(foundKorisnik.isPresent());
        assertEquals(korisnik.getId(), foundKorisnik.get().getId());
        assertEquals(korisnik.getIme(), foundKorisnik.get().getIme());
        assertEquals(korisnik.getPrezime(), foundKorisnik.get().getPrezime());

        verify(korisnikRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(korisnikRepository.findById(1)).thenReturn(Optional.empty());

        // Testiranje metode
        Optional<Korisnik> foundKorisnik = korisnikServis.findById(1);

        assertFalse(foundKorisnik.isPresent());

        verify(korisnikRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveKorisnik() {
        Korisnik korisnikToSave = new Korisnik();
        korisnikToSave.setKorisnikUuid(UUID.randomUUID());
        korisnikToSave.setIme("NovoIme");
        korisnikToSave.setPrezime("NovoPrezime");
        korisnikToSave.setEmail("novo@example.com");

        Korisnik savedKorisnik = new Korisnik();
        savedKorisnik.setId(1); // Pretpostavljam da se ID generiše prilikom čuvanja
        savedKorisnik.setKorisnikUuid(korisnikToSave.getKorisnikUuid());
        savedKorisnik.setIme(korisnikToSave.getIme());
        savedKorisnik.setPrezime(korisnikToSave.getPrezime());
        savedKorisnik.setEmail(korisnikToSave.getEmail());

        when(korisnikRepository.save(any(Korisnik.class))).thenReturn(savedKorisnik);

        // Testiranje metode
        Korisnik result = korisnikServis.saveKorisnik(korisnikToSave);

        assertNotNull(result);
        assertEquals(savedKorisnik.getId(), result.getId());
        assertEquals(korisnikToSave.getKorisnikUuid(), result.getKorisnikUuid());
        assertEquals(korisnikToSave.getIme(), result.getIme());
        assertEquals(korisnikToSave.getPrezime(), result.getPrezime());
        assertEquals(korisnikToSave.getEmail(), result.getEmail());

        verify(korisnikRepository, times(1)).save(any(Korisnik.class));
    }

    @Test
    public void testDeleteById() {
        int korisnikIdToDelete = 1;
        doNothing().when(korisnikRepository).deleteById(korisnikIdToDelete);

        // Testiranje metode
        korisnikServis.deleteById(korisnikIdToDelete);

        verify(korisnikRepository, times(1)).deleteById(korisnikIdToDelete);
    }
}


