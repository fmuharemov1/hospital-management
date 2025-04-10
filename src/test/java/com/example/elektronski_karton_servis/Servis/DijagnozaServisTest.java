package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.example.elektronski_karton_servis.Repository.DijagnozaRepository;
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

public class DijagnozaServisTest {

    @Mock
    private DijagnozaRepository dijagnozaRepository;

    @InjectMocks
    private DijagnozaServis dijagnozaServis;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllDijagnoze() {
        Dijagnoza dijagnoza1 = new Dijagnoza();
        dijagnoza1.setId(1); // Pretpostavljam da je ID Integer
        dijagnoza1.setKartonId(101);
        dijagnoza1.setNaziv("Prehlada");
        dijagnoza1.setDatumDijagnoze(LocalDateTime.now());

        Dijagnoza dijagnoza2 = new Dijagnoza();
        dijagnoza2.setId(2); // Pretpostavljam da je ID Integer
        dijagnoza2.setKartonId(101);
        dijagnoza2.setNaziv("Gripa");
        dijagnoza2.setDatumDijagnoze(LocalDateTime.now().minusDays(1));

        when(dijagnozaRepository.findAll()).thenReturn(Arrays.asList(dijagnoza1, dijagnoza2));

        // Testiranje metode
        var dijagnoze = dijagnozaServis.getAllDijagnoze();

        assertNotNull(dijagnoze);
        assertEquals(2, dijagnoze.size());
        assertEquals(dijagnoza1.getId(), dijagnoze.get(0).getId());
        assertEquals(dijagnoza1.getKartonId(), dijagnoze.get(0).getKartonId());
        assertEquals(dijagnoza1.getNaziv(), dijagnoze.get(0).getNaziv());
        assertEquals(dijagnoza2.getId(), dijagnoze.get(1).getId());
        assertEquals(dijagnoza2.getNaziv(), dijagnoze.get(1).getNaziv());

        verify(dijagnozaRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Dijagnoza dijagnoza = new Dijagnoza();
        dijagnoza.setId(1); // Pretpostavljam da je ID Integer
        dijagnoza.setKartonId(102);
        dijagnoza.setNaziv("Alergija");
        dijagnoza.setDatumDijagnoze(LocalDateTime.now());

        when(dijagnozaRepository.findById(1)).thenReturn(Optional.of(dijagnoza));

        // Testiranje metode
        Optional<Dijagnoza> foundDijagnoza = dijagnozaServis.findById(1);

        assertTrue(foundDijagnoza.isPresent());
        assertEquals(dijagnoza.getId(), foundDijagnoza.get().getId());
        assertEquals(dijagnoza.getKartonId(), foundDijagnoza.get().getKartonId());
        assertEquals(dijagnoza.getNaziv(), foundDijagnoza.get().getNaziv());

        verify(dijagnozaRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(dijagnozaRepository.findById(1)).thenReturn(Optional.empty());

        // Testiranje metode
        Optional<Dijagnoza> foundDijagnoza = dijagnozaServis.findById(1);

        assertFalse(foundDijagnoza.isPresent());

        verify(dijagnozaRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveDijagnoza() {
        Dijagnoza dijagnozaToSave = new Dijagnoza();
        dijagnozaToSave.setKartonId(103);
        dijagnozaToSave.setNaziv("Sinusitis");
        dijagnozaToSave.setOpis("Upala sinusa");
        dijagnozaToSave.setDatumDijagnoze(LocalDateTime.now());

        Dijagnoza savedDijagnoza = new Dijagnoza();
        savedDijagnoza.setId(1); // Pretpostavljam da se ID generiše prilikom čuvanja
        savedDijagnoza.setKartonId(dijagnozaToSave.getKartonId());
        savedDijagnoza.setNaziv(dijagnozaToSave.getNaziv());
        savedDijagnoza.setOpis(dijagnozaToSave.getOpis());
        savedDijagnoza.setDatumDijagnoze(dijagnozaToSave.getDatumDijagnoze());

        when(dijagnozaRepository.save(any(Dijagnoza.class))).thenReturn(savedDijagnoza);

        // Testiranje metode
        Dijagnoza result = dijagnozaServis.saveDijagnoza(dijagnozaToSave);

        assertNotNull(result);
        assertEquals(savedDijagnoza.getId(), result.getId());
        assertEquals(dijagnozaToSave.getKartonId(), result.getKartonId());
        assertEquals(dijagnozaToSave.getNaziv(), result.getNaziv());
        assertEquals(dijagnozaToSave.getOpis(), result.getOpis());

        verify(dijagnozaRepository, times(1)).save(any(Dijagnoza.class));
    }

    @Test
    public void testDeleteById() {
        int dijagnozaIdToDelete = 1;
        doNothing().when(dijagnozaRepository).deleteById(dijagnozaIdToDelete);

        // Testiranje metode
        dijagnozaServis.deleteById(dijagnozaIdToDelete);

        verify(dijagnozaRepository, times(1)).deleteById(dijagnozaIdToDelete);
    }
}



