package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.service.KorisnikService;
import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class KorisnikControllerTest {

    private MockMvc mockMvc;

    @Mock
    private KorisnikService korisnikService;

    @InjectMocks
    private KorisnikController korisnikController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(korisnikController).build();
    }

    @Test
    void testGetAllKorisnici() throws Exception {
        // Arrange
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setIme("Test");
        korisnik.setPrezime("User");
        korisnik.setEmail("test@example.com");
        korisnik.setLozinka("password");

        when(korisnikService.getAllKorisnici()).thenReturn(Collections.singletonList(korisnik));

        // Act & Assert
        mockMvc.perform(get("/korisnici"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].ime").value("Test"))
                .andExpect(jsonPath("$[0].prezime").value("User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    void testCreateKorisnik() throws Exception {
        // Arrange
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setIme("Test");
        korisnik.setPrezime("User");
        korisnik.setEmail("test@example.com");
        korisnik.setLozinka("password");

        when(korisnikService.saveKorisnik(any(Korisnik.class))).thenReturn(korisnik);

        // Act & Assert
        mockMvc.perform(post("/korisnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ime\":\"Test\", \"prezime\":\"User\", \"email\":\"test@example.com\", \"lozinka\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ime").value("Test"))
                .andExpect(jsonPath("$.prezime").value("User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetKorisnikById() throws Exception {
        // Arrange
        Korisnik korisnik = new Korisnik();
        korisnik.setId(1L);
        korisnik.setIme("Test");
        korisnik.setPrezime("User");
        korisnik.setEmail("test@example.com");
        korisnik.setLozinka("password");

        when(korisnikService.findById(1L)).thenReturn(Optional.of(korisnik));

        // Act & Assert
        mockMvc.perform(get("/korisnici/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ime").value("Test"))
                .andExpect(jsonPath("$.prezime").value("User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetKorisnikByIdNotFound() throws Exception {
        // Arrange
        when(korisnikService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/korisnici/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Korisnik not found"))
                .andExpect(jsonPath("$.message").value("Korisnik sa ID 1 nije pronađen."));
    }

    @Test
    void testReplaceKorisnik() throws Exception {
        // Arrange
        Korisnik existingKorisnik = new Korisnik();
        existingKorisnik.setId(1L);
        existingKorisnik.setIme("Test");
        existingKorisnik.setPrezime("User");
        existingKorisnik.setEmail("test@example.com");
        existingKorisnik.setLozinka("password");

        Korisnik updatedKorisnik = new Korisnik();
        updatedKorisnik.setIme("Updated");
        updatedKorisnik.setPrezime("User");
        updatedKorisnik.setEmail("updated@example.com");
        updatedKorisnik.setLozinka("newpassword");

        when(korisnikService.findById(1L)).thenReturn(Optional.of(existingKorisnik));
        when(korisnikService.saveKorisnik(any(Korisnik.class))).thenReturn(updatedKorisnik);

        // Act & Assert
        mockMvc.perform(put("/korisnici/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ime\":\"Updated\", \"prezime\":\"User\", \"email\":\"updated@example.com\", \"lozinka\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ime").value("Updated"))
                .andExpect(jsonPath("$.prezime").value("User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteKorisnik() throws Exception {
        // Act & Assert
        doNothing().when(korisnikService).deleteById(1L);

        mockMvc.perform(delete("/korisnici/1"))
                .andExpect(status().isOk());

        verify(korisnikService, times(1)).deleteById(1L);
    }
}

