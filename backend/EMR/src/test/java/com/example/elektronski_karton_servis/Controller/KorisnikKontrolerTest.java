package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Controller.KorisnikKontroler;
import com.example.elektronski_karton_servis.Servis.KorisnikServis;
import com.example.elektronski_karton_servis.model.Korisnik;
import com.example.logging.GrpcSystemEventsClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ActiveProfiles("test")

@WebMvcTest(KorisnikKontroler.class)
public class KorisnikKontrolerTest {
    @MockBean
    private GrpcSystemEventsClient grpcSystemEventsClient;

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private KorisnikServis servis;

    private Korisnik sample() {
        Korisnik k = new Korisnik("Ime", "Prezime", "test@test.com", "lozinka", "061111111", "Adresa", 1);
        k.setId(1);
        return k;
    }

    @Test
    void all() throws Exception {
        Mockito.when(servis.getAllKorisnici()).thenReturn(List.of(sample()));
        mockMvc.perform(get("/korisnici")).andExpect(status().isOk());
    }

    @Test
    void one_found() throws Exception {
        Mockito.when(servis.findById(1)).thenReturn(Optional.of(sample()));
        mockMvc.perform(get("/korisnici/1")).andExpect(status().isOk());
    }

    @Test
    void one_not_found() throws Exception {
        Mockito.when(servis.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(get("/korisnici/1")).andExpect(status().isNotFound());
    }

    @Test
    void create_valid() throws Exception {
        Korisnik k = sample();
        Mockito.when(servis.saveKorisnik(any())).thenReturn(k);
        mockMvc.perform(post("/korisnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_invalid() throws Exception {
        Korisnik k = new Korisnik("", "", "", "", "", "", 1);
        mockMvc.perform(post("/korisnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        Korisnik k = sample();
        Mockito.when(servis.findById(1)).thenReturn(Optional.of(k));
        Mockito.when(servis.saveKorisnik(any())).thenReturn(k);
        mockMvc.perform(put("/korisnici/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_ok() throws Exception {
        mockMvc.perform(delete("/korisnici/1")).andExpect(status().isNoContent());
    }

    @Test
    void getPaged() throws Exception {
        Mockito.when(servis.getAllKorisniciPaged(any())).thenReturn(new PageImpl<>(List.of(sample())));
        mockMvc.perform(get("/korisnici/paged?page=0&size=10"))
                .andExpect(status().isOk());
    }

    @Test
    void byImePrezime() throws Exception {
        Mockito.when(servis.findByImePrezime("Ime", "Prezime")).thenReturn(List.of(sample()));
        mockMvc.perform(get("/korisnici/byImePrezime?ime=Ime&prezime=Prezime"))
                .andExpect(status().isOk());
    }

    @Test
    void bySadrzaj() throws Exception {
        Mockito.when(servis.findByImeIliPrezimeSadrzi("I")).thenReturn(List.of(sample()));
        mockMvc.perform(get("/korisnici/byImeIliPrezimeSadrzi?tekst=I"))
                .andExpect(status().isOk());
    }

    @Test
    void byUloga() throws Exception {
        Mockito.when(servis.findByUloga(1)).thenReturn(List.of(sample()));
        mockMvc.perform(get("/korisnici/byUloga/1")).andExpect(status().isOk());
    }
}