package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.service.KorisnikService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KorisnikController.class)
public class KorisnikControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KorisnikService korisnikService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllKorisnici_ShouldReturnList() throws Exception {
        Korisnik k1 = new Korisnik();
        k1.setId(1L);
        k1.setKorisnikUuid(UUID.randomUUID());

        Korisnik k2 = new Korisnik();
        k2.setId(2L);
        k2.setKorisnikUuid(UUID.randomUUID());

        Mockito.when(korisnikService.getAllKorisnici()).thenReturn(List.of(k1, k2));

        mockMvc.perform(get("/korisnici"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getKorisnikById_ShouldReturnKorisnik() throws Exception {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setKorisnikUuid(UUID.randomUUID());

        Mockito.when(korisnikService.findById(1L)).thenReturn(Optional.of(korisnik));

        mockMvc.perform(get("/korisnici/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getKorisnikById_NotFound() throws Exception {
        Mockito.when(korisnikService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/korisnici/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createKorisnik_ShouldReturnCreated() throws Exception {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setKorisnikUuid(UUID.randomUUID());
        korisnik.setRole(new Role()); // Dodaj ako treba validacija

        Mockito.when(korisnikService.saveKorisnik(any(Korisnik.class))).thenReturn(korisnik);

        mockMvc.perform(post("/korisnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(korisnik)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateKorisnik_ShouldReturnUpdated() throws Exception {
        Korisnik old = new Korisnik();
        old.setId(1L);
        old.setKorisnikUuid(UUID.randomUUID());
        old.setRole(new Role());

        Korisnik updated = new Korisnik();
        updated.setId(1L);
        updated.setKorisnikUuid(UUID.randomUUID());
        updated.setRole(new Role());

        Mockito.when(korisnikService.findById(1L)).thenReturn(Optional.of(old));
        Mockito.when(korisnikService.saveKorisnik(any())).thenReturn(updated);

        mockMvc.perform(put("/korisnici/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteKorisnik_ShouldReturnNoContent() throws Exception {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);

        Mockito.when(korisnikService.findById(1L)).thenReturn(Optional.of(korisnik));
        doNothing().when(korisnikService).deleteById(1L);

        mockMvc.perform(delete("/korisnici/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteKorisnik_NotFound() throws Exception {
        Long nepostojeciId = 1L;

        // Simuliraj da korisnik ne postoji
        doThrow(new KorisnikNotFoundException(nepostojeciId))
                .when(korisnikService).deleteById(nepostojeciId);

        mockMvc.perform(delete("/korisnici/{id}", nepostojeciId))
                .andExpect(status().isNotFound());  // 404
    }
}
