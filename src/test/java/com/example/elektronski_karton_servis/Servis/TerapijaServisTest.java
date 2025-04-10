package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Terapija;
import com.example.elektronski_karton_servis.Repository.TerapijaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TerapijaServisTest {

    @Mock
    private TerapijaRepository terapijaRepository;

    @InjectMocks
    private TerapijaServis terapijaServis;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTerapije() {
        Terapija terapija1 = new Terapija();
        terapija1.setId(1);
        terapija1.setOsobljeUuid(1); // Koristite int
        terapija1.setNaziv("Paracetamol");
        terapija1.setOpis("Za bolove");
        terapija1.setDatumPocetka(LocalDateTime.now());

        Terapija terapija2 = new Terapija();
        terapija2.setId(2);
        terapija2.setOsobljeUuid(2); // Koristite int
        terapija2.setNaziv("Ibuprofen");
        terapija2.setOpis("Protiv upala");
        terapija2.setDatumPocetka(LocalDateTime.now().minusDays(1));

        when(terapijaRepository.findAll()).thenReturn(Arrays.asList(terapija1, terapija2));

        var terapije = terapijaServis.getAllTerapije();

        assertNotNull(terapije);
        assertEquals(2, terapije.size());
        assertEquals(terapija1.getId(), terapije.get(0).getId());
        assertEquals(terapija1.getOsobljeUuid(), terapije.get(0).getOsobljeUuid());
        assertEquals(terapija1.getNaziv(), terapije.get(0).getNaziv());
        assertEquals(terapija1.getOpis(), terapije.get(0).getOpis()); // Dodano
        assertEquals(terapija2.getId(), terapije.get(1).getId());
        assertEquals(terapija2.getOsobljeUuid(), terapije.get(1).getOsobljeUuid());
        assertEquals(terapija2.getNaziv(), terapije.get(1).getNaziv());
        assertEquals(terapija2.getOpis(), terapije.get(1).getOpis()); // Dodano

        verify(terapijaRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Terapija terapija = new Terapija();
        terapija.setId(1);
        terapija.setOsobljeUuid(1); // Koristite int
        terapija.setNaziv("Amoksicilin");
        terapija.setOpis("Antibiotik");
        terapija.setDatumPocetka(LocalDateTime.now());

        when(terapijaRepository.findById(1)).thenReturn(Optional.of(terapija));

        Optional<Terapija> foundTerapija = terapijaServis.findById(1);

        assertTrue(foundTerapija.isPresent());
        assertEquals(terapija.getId(), foundTerapija.get().getId());
        assertEquals(terapija.getOsobljeUuid(), foundTerapija.get().getOsobljeUuid());
        assertEquals(terapija.getNaziv(), foundTerapija.get().getNaziv());
        assertEquals(terapija.getOpis(), foundTerapija.get().getOpis()); // Dodano

        verify(terapijaRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveTerapija() {
        Terapija terapijaToSave = new Terapija();
        terapijaToSave.setOsobljeUuid(1); // Koristite int
        terapijaToSave.setNaziv("Fizikalna terapija");
        terapijaToSave.setOpis("Vježbe za leđa");
        terapijaToSave.setDatumPocetka(LocalDateTime.now());
        terapijaToSave.setDatumZavrsetka(LocalDateTime.now().plusWeeks(2));


        Terapija savedTerapija = new Terapija();
        savedTerapija.setId(1);
        savedTerapija.setOsobljeUuid(terapijaToSave.getOsobljeUuid());
        savedTerapija.setNaziv(terapijaToSave.getNaziv());
        savedTerapija.setOpis(terapijaToSave.getOpis());
        savedTerapija.setDatumPocetka(terapijaToSave.getDatumPocetka());
        savedTerapija.setDatumZavrsetka(terapijaToSave.getDatumZavrsetka());


        when(terapijaRepository.save(any(Terapija.class))).thenReturn(savedTerapija);

        Terapija result = terapijaServis.saveTerapija(terapijaToSave);

        assertNotNull(result);
        assertEquals(savedTerapija.getId(), result.getId());
        assertEquals(terapijaToSave.getOsobljeUuid(), result.getOsobljeUuid());
        assertEquals(terapijaToSave.getNaziv(), result.getNaziv());
        assertEquals(terapijaToSave.getOpis(), result.getOpis());
        assertEquals(terapijaToSave.getDatumPocetka(), result.getDatumPocetka());
        assertEquals(terapijaToSave.getDatumZavrsetka(), result.getDatumZavrsetka());

        verify(terapijaRepository, times(1)).save(any(Terapija.class));
    }

    @Test
    public void testDeleteById() {
        int terapijaIdToDelete = 1;
        doNothing().when(terapijaRepository).deleteById(terapijaIdToDelete);

        terapijaServis.deleteById(terapijaIdToDelete);

        verify(terapijaRepository, times(1)).deleteById(terapijaIdToDelete);
    }
}