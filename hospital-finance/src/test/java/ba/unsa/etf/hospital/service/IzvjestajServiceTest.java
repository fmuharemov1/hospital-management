package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.IzvjestajNotFoundException;
import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.repository.IzvjestajRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IzvjestajServiceTest {

    @Mock
    private IzvjestajRepository izvjestajRepository;

    @InjectMocks
    private IzvjestajService izvjestajService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllIzvjestaji() {
        Izvjestaj izvjestaj1 = new Izvjestaj();
        izvjestaj1.setId(1L);
        izvjestaj1.setTipIzvjestaja("Izvjestaj 1");

        Izvjestaj izvjestaj2 = new Izvjestaj();
        izvjestaj2.setId(2L);
        izvjestaj2.setTipIzvjestaja("Izvjestaj 2");

        when(izvjestajRepository.findAll()).thenReturn(Arrays.asList(izvjestaj1, izvjestaj2));

        // Testiranje metode
        var izvjestaji = izvjestajService.getAllIzvjestaji();

        assertNotNull(izvjestaji);
        assertEquals(2, izvjestaji.size());
        assertEquals("Izvjestaj 1", izvjestaji.get(0).getTipIzvjestaja());
        assertEquals("Izvjestaj 2", izvjestaji.get(1).getTipIzvjestaja());

        verify(izvjestajRepository, times(1)).findAll();
    }

    @Test
    public void testSaveIzvjestaj() {
        Izvjestaj izvjestaj = new Izvjestaj();
        izvjestaj.setTipIzvjestaja("Izvjestaj 3");

        when(izvjestajRepository.save(any(Izvjestaj.class))).thenReturn(izvjestaj);

        // Testiranje metode
        Izvjestaj savedIzvjestaj = izvjestajService.saveIzvjestaj(izvjestaj);

        assertNotNull(savedIzvjestaj);
        assertEquals("Izvjestaj 3", savedIzvjestaj.getTipIzvjestaja());

        verify(izvjestajRepository, times(1)).save(any(Izvjestaj.class));
    }

    @Test
    public void testFindById() {
        Izvjestaj izvjestaj = new Izvjestaj();
        izvjestaj.setId(1L);
        izvjestaj.setTipIzvjestaja("Izvjestaj 1");

        when(izvjestajRepository.findById(1L)).thenReturn(Optional.of(izvjestaj));

        // Testiranje metode
        Optional<Izvjestaj> foundIzvjestaj = izvjestajService.findById(1L);

        assertTrue(foundIzvjestaj.isPresent());
        assertEquals("Izvjestaj 1", foundIzvjestaj.get().getTipIzvjestaja());

        verify(izvjestajRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById_Izvjestaj() {
        Long id = 1L;

        Izvjestaj izvjestaj = new Izvjestaj();
        izvjestaj.setId(id);

        when(izvjestajRepository.findById(id)).thenReturn(Optional.of(izvjestaj));
        doNothing().when(izvjestajRepository).deleteById(id);

        izvjestajService.deleteById(id);

        verify(izvjestajRepository, times(1)).findById(id);
        verify(izvjestajRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteById_Izvjestaj_NotFound() {
        Long id = 1L;

        when(izvjestajRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IzvjestajNotFoundException.class, () -> izvjestajService.deleteById(id));

        verify(izvjestajRepository, times(1)).findById(id);
        verify(izvjestajRepository, never()).deleteById(id);
    }
    @Test
    public void testGetIzvjestajiByTipAndMinIznos() {
        Izvjestaj izvjestaj = new Izvjestaj();
        izvjestaj.setTipIzvjestaja("Mjesecni");
        izvjestaj.setFinansijskiPregled(2000.00);

        when(izvjestajRepository.findIzvjestajiByTipAndIznosGreaterThan("Mjesecni", 1000.00))
                .thenReturn(Arrays.asList(izvjestaj));

        List<Izvjestaj> result = izvjestajService.getIzvjestajiByTipAndMinIznos("Mjesecni", 1000.00);

        assertEquals(1, result.size());
        assertEquals("Mjesecni", result.get(0).getTipIzvjestaja());
    }

}
