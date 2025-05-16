package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Role;
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
import java.util.UUID;

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
        korisnik.setLozinka("Password1");  // Lozinka sada sadrži veliko slovo, malo slovo i broj
        korisnik.setKorisnikUuid(UUID.randomUUID());  // Generišemo UUID
        Role role = new Role();
        role.setTipKorisnika("Admin");  // Primer vrednosti za tip korisnika
        role.setSmjena("Prva smjena");  // Primer vrednosti za smjenu
        role.setOdjeljenje("Interna");  // Primer vrednosti za odjeljenje
        korisnik.setRole(role);  // Postavljamo rolu
        korisnik.setBr_telefona("1234567890");  // Dodajemo broj telefona

        when(korisnikService.saveKorisnik(any(Korisnik.class))).thenReturn(korisnik);

        // Act & Assert
        mockMvc.perform(post("/korisnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ime\":\"Test\", \"prezime\":\"User\", \"email\":\"test@example.com\", \"lozinka\":\"Password1\", \"korisnikUuid\":\"" + korisnik.getKorisnikUuid() + "\", \"role\":{\"tipKorisnika\":\"Admin\", \"smjena\":\"Prva smjena\", \"odjeljenje\":\"Interna\"}, \"br_telefona\":\"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ime").value("Test"))
                .andExpect(jsonPath("$.prezime").value("User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.korisnikUuid").value(korisnik.getKorisnikUuid().toString()))
                .andExpect(jsonPath("$.role.tipKorisnika").value("Admin"))
                .andExpect(jsonPath("$.role.smjena").value("Prva smjena"))
                .andExpect(jsonPath("$.role.odjeljenje").value("Interna"))
                .andExpect(jsonPath("$.br_telefona").value("1234567890"));
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
                .andExpect(status().isNotFound());
    }

    @Test
    void testReplaceKorisnik() throws Exception {
        // Arrange
        Korisnik existingKorisnik = new Korisnik();
        existingKorisnik.setId(1L);
        existingKorisnik.setIme("Test");
        existingKorisnik.setPrezime("User");
        existingKorisnik.setEmail("test@example.com");
        existingKorisnik.setLozinka("Password1");  // Lozinka sa velikim i malim slovom i brojem
        existingKorisnik.setKorisnikUuid(UUID.randomUUID());  // Generišemo UUID
        Role role = new Role();
        role.setTipKorisnika("Admin");
        role.setSmjena("Prva smjena");
        role.setOdjeljenje("Interna");
        existingKorisnik.setRole(role);
        existingKorisnik.setBr_telefona("1234567890");

        Korisnik updatedKorisnik = new Korisnik();
        updatedKorisnik.setId(1L);
        updatedKorisnik.setIme("Updated");
        updatedKorisnik.setPrezime("User");
        updatedKorisnik.setEmail("updated@example.com");
        updatedKorisnik.setLozinka("NewPassword1");  // Lozinka sa velikim i malim slovom i brojem
        updatedKorisnik.setKorisnikUuid(existingKorisnik.getKorisnikUuid());
        updatedKorisnik.setRole(role);
        updatedKorisnik.setBr_telefona("0987654321");

        when(korisnikService.findById(1L)).thenReturn(Optional.of(existingKorisnik));
        when(korisnikService.saveKorisnik(any(Korisnik.class))).thenReturn(updatedKorisnik);

        // Act & Assert
        mockMvc.perform(put("/korisnici/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ime\":\"Updated\", \"prezime\":\"User\", \"email\":\"updated@example.com\", \"lozinka\":\"NewPassword1\", \"korisnikUuid\":\"" + existingKorisnik.getKorisnikUuid() + "\", \"role\":{\"tipKorisnika\":\"Admin\", \"smjena\":\"Prva smjena\", \"odjeljenje\":\"Interna\"}, \"br_telefona\":\"0987654321\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ime").value("Updated"))
                .andExpect(jsonPath("$.prezime").value("User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.korisnikUuid").value(existingKorisnik.getKorisnikUuid().toString()))
                .andExpect(jsonPath("$.role.tipKorisnika").value("Admin"))
                .andExpect(jsonPath("$.role.smjena").value("Prva smjena"))
                .andExpect(jsonPath("$.role.odjeljenje").value("Interna"))
                .andExpect(jsonPath("$.br_telefona").value("0987654321"));
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

