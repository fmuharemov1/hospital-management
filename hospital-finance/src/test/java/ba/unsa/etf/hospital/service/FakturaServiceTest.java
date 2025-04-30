package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.repository.FakturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FakturaServiceTest {

    @Mock
    private FakturaRepository fakturaRepository;

    @InjectMocks
    private FakturaService fakturaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllFakture() {
        Faktura faktura1 = new Faktura();
        faktura1.setId(1L);
        faktura1.setIznos(100.0);
        faktura1.setStatus("Neplaceno");
        faktura1.setMetod("Gotovina");

        Faktura faktura2 = new Faktura();
        faktura2.setId(2L);
        faktura2.setIznos(200.0);
        faktura2.setStatus("Placeno");
        faktura2.setMetod("Kartica");

        when(fakturaRepository.findAll()).thenReturn(Arrays.asList(faktura1, faktura2));

        // Testiranje metode getAllFakture()
        var fakture = fakturaService.getAllFakture();

        assertNotNull(fakture);
        assertEquals(2, fakture.size());
        assertEquals(1L, fakture.get(0).getId());
        assertEquals("Placeno", fakture.get(1).getStatus());

        verify(fakturaRepository, times(1)).findAll();
    }

    @Test
    public void testSaveFaktura() {
        Faktura faktura = new Faktura();
        faktura.setIznos(150.0);
        faktura.setStatus("Neplaceno");
        faktura.setMetod("Debit");

        when(fakturaRepository.save(faktura)).thenReturn(faktura);

        // Testiranje metode saveFaktura()
        Faktura savedFaktura = fakturaService.saveFaktura(faktura);

        assertNotNull(savedFaktura);
        assertEquals(150.0, savedFaktura.getIznos());
        assertEquals("Debit", savedFaktura.getMetod());
        assertEquals(faktura.getIznos(), savedFaktura.getIznos()); // Provjera iznosa

        verify(fakturaRepository, times(1)).save(faktura);
    }

    @Test
    public void testFindById() {
        Faktura faktura = new Faktura();
        faktura.setId(1L);
        faktura.setIznos(100.0);
        faktura.setStatus("Neplaceno");
        faktura.setMetod("Gotovina");

        when(fakturaRepository.findById(1L)).thenReturn(Optional.of(faktura));

        // Testiranje metode findById()
        Optional<Faktura> foundFaktura = fakturaService.findById(1L);

        assertTrue(foundFaktura.isPresent());
        assertEquals(1L, foundFaktura.get().getId());
        assertEquals("Neplaceno", foundFaktura.get().getStatus());

        verify(fakturaRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        Faktura faktura = new Faktura();
        faktura.setId(id);

        // Mockiramo da entitet postoji
        when(fakturaRepository.findById(id)).thenReturn(Optional.of(faktura));
        doNothing().when(fakturaRepository).deleteById(id);

        // Poziv metode
        fakturaService.deleteById(id);

        // Verifikacija
        verify(fakturaRepository, times(1)).findById(id);
        verify(fakturaRepository, times(1)).deleteById(id);
    }
    @Test
    public void testDeleteById_NotFound() {
        Long id = 1L;

        // Simuliramo da faktura ne postoji
        when(fakturaRepository.findById(id)).thenReturn(Optional.empty());

        // Provjeravamo da baca izuzetak
        assertThrows(FakturaNotFoundException.class, () -> fakturaService.deleteById(id));

        // deleteById se ne smije pozvati jer faktura nije pronađena
        verify(fakturaRepository, times(1)).findById(id);
        verify(fakturaRepository, never()).deleteById(id);
    }
}
