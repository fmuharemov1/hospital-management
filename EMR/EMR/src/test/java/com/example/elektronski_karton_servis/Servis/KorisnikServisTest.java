package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Exception.KorisnikNotFoundException;
import com.example.elektronski_karton_servis.Repository.KorisnikRepository;
import com.example.elektronski_karton_servis.Servis.KorisnikServis;
import com.example.elektronski_karton_servis.model.Korisnik;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KorisnikServisTest {

    private KorisnikRepository repo;
    private ObjectMapper objectMapper;
    private KorisnikServis servis;

    @BeforeEach
    void setup() {
        repo = Mockito.mock(KorisnikRepository.class);
        objectMapper = new ObjectMapper();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        servis = new KorisnikServis(repo, objectMapper, validator);
    }

    private Korisnik sample() {
        return new Korisnik("Ime", "Prezime", "test@test.com", "lozinka", "061111111", "Adresa", 1);
    }

    @Test
    void getAll() {
        when(repo.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, servis.getAllKorisnici().size());
    }

    @Test
    void findById_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample()));
        assertTrue(servis.findById(1).isPresent());
    }

    @Test
    void findById_notFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertTrue(servis.findById(1).isEmpty());
    }

    @Test
    void save() {
        Korisnik k = sample();
        when(repo.save(k)).thenReturn(k);
        assertEquals(k, servis.saveKorisnik(k));
    }

    @Test
    void delete() {
        servis.deleteById(1);
        verify(repo, times(1)).deleteById(1);
    }

    @Test
    void getPaged() {
        when(repo.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(sample())));
        assertEquals(1, servis.getAllKorisniciPaged(PageRequest.of(0, 10)).getTotalElements());
    }

    @Test
    void findByImePrezime() {
        when(repo.findByImeIgnoreCaseAndPrezimeIgnoreCase("Ime", "Prezime")).thenReturn(List.of(sample()));
        assertEquals(1, servis.findByImePrezime("Ime", "Prezime").size());
    }

    @Test
    void findBySadrzaj() {
        when(repo.findByImeIliPrezimeSadrziIgnoreCase("im")).thenReturn(List.of(sample()));
        assertEquals(1, servis.findByImeIliPrezimeSadrzi("im").size());
    }

    @Test
    void findByUloga() {
        when(repo.findByRoleId(1)).thenReturn(List.of(sample()));
        assertEquals(1, servis.findByUloga(1).size());
    }


}