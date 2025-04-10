package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.service.TerminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TerminControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TerminService terminService;

    @InjectMocks
    private TerminController terminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(terminController).build();
    }

    @Test
    void testGetAllTermini() throws Exception {
        // Arrange
        Korisnik pacijent = new Korisnik();
        pacijent.setIme("John");
        pacijent.setPrezime("Doe");
        pacijent.setEmail("john.doe@example.com");
        pacijent.setKorisnikUuid(UUID.randomUUID());

        Korisnik osoblje = new Korisnik();
        osoblje.setIme("Dr. Smith");
        osoblje.setPrezime("Johnson");
        osoblje.setEmail("dr.smith@example.com");
        osoblje.setKorisnikUuid(UUID.randomUUID());

        Obavijest obavijest = new Obavijest();
        obavijest.setSadrzaj("Checkup");

        Termin termin = new Termin();
        termin.setId(1L);
        termin.setPacijent(pacijent);
        termin.setOsoblje(osoblje);
        termin.setObavijest(obavijest);
        termin.setStatus("Scheduled");
        termin.setDatumVrijeme(LocalDateTime.parse("2025-04-01T09:00:00"));
        termin.setTrajanje(30);
        termin.setMeet_link("http://example.com/meet");

        when(terminService.getAllTermini()).thenReturn(Collections.singletonList(termin));

        // Act & Assert
        mockMvc.perform(get("/termini"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].pacijent.ime").value("John"))
                .andExpect(jsonPath("$[0].pacijent.prezime").value("Doe"))
                .andExpect(jsonPath("$[0].osoblje.ime").value("Dr. Smith"))
                .andExpect(jsonPath("$[0].osoblje.prezime").value("Johnson"))
                .andExpect(jsonPath("$[0].obavijest.sadrzaj").value("Checkup"))
                .andExpect(jsonPath("$[0].status").value("Scheduled"))
                .andExpect(jsonPath("$[0].datumVrijeme").value("2025-04-01T09:00:00"))
                .andExpect(jsonPath("$[0].meet_link").value("http://example.com/meet"));
    }

    @Test
    void testCreateTermin() throws Exception {
        // Arrange
        Korisnik pacijent = new Korisnik();
        pacijent.setIme("John");
        pacijent.setPrezime("Doe");
        pacijent.setEmail("john.doe@example.com");
        pacijent.setKorisnikUuid(UUID.randomUUID());

        Korisnik osoblje = new Korisnik();
        osoblje.setIme("Dr. Smith");
        osoblje.setPrezime("Johnson");
        osoblje.setEmail("dr.smith@example.com");
        osoblje.setKorisnikUuid(UUID.randomUUID());

        Obavijest obavijest = new Obavijest();
        obavijest.setSadrzaj("Checkup");

        Termin termin = new Termin();
        termin.setId(1L);
        termin.setPacijent(pacijent);
        termin.setOsoblje(osoblje);
        termin.setObavijest(obavijest);
        termin.setStatus("Scheduled");
        termin.setDatumVrijeme(LocalDateTime.parse("2025-04-01T09:00:00"));
        termin.setTrajanje(30);
        termin.setMeet_link("http://example.com/meet");

        when(terminService.saveTermin(any(Termin.class))).thenReturn(termin);

        // Act & Assert
        mockMvc.perform(post("/termini")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pacijent\":{\"ime\":\"John\",\"prezime\":\"Doe\",\"email\":\"john.doe@example.com\",\"korisnikUuid\":\"" + pacijent.getKorisnikUuid() + "\"}, \"osoblje\":{\"ime\":\"Dr. Smith\",\"prezime\":\"Johnson\",\"email\":\"dr.smith@example.com\",\"korisnikUuid\":\"" + osoblje.getKorisnikUuid() + "\"}, \"obavijest\":{\"sadrzaj\":\"Checkup\"}, \"status\":\"Scheduled\", \"datumVrijeme\":\"2025-04-01T09:00:00\", \"trajanjes\":30, \"meet_link\":\"http://example.com/meet\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pacijent.ime").value("John"))
                .andExpect(jsonPath("$.osoblje.ime").value("Dr. Smith"))
                .andExpect(jsonPath("$.obavijest.sadrzaj").value("Checkup"))
                .andExpect(jsonPath("$.status").value("Scheduled"))
                .andExpect(jsonPath("$.datumVrijeme").value("2025-04-01T09:00:00"))
                .andExpect(jsonPath("$.meet_link").value("http://example.com/meet"));
    }

    @Test
    void testGetTerminById() throws Exception {
        // Arrange
        Korisnik pacijent = new Korisnik();
        pacijent.setIme("John");
        pacijent.setPrezime("Doe");
        pacijent.setEmail("john.doe@example.com");
        pacijent.setKorisnikUuid(UUID.randomUUID());

        Korisnik osoblje = new Korisnik();
        osoblje.setIme("Dr. Smith");
        osoblje.setPrezime("Johnson");
        osoblje.setEmail("dr.smith@example.com");
        osoblje.setKorisnikUuid(UUID.randomUUID());

        Obavijest obavijest = new Obavijest();
        obavijest.setSadrzaj("Checkup");

        Termin termin = new Termin();
        termin.setId(1L);
        termin.setPacijent(pacijent);
        termin.setOsoblje(osoblje);
        termin.setObavijest(obavijest);
        termin.setStatus("Scheduled");
        termin.setDatumVrijeme(LocalDateTime.parse("2025-04-01T09:00:00"));
        termin.setTrajanje(30);
        termin.setMeet_link("http://example.com/meet");

        when(terminService.findById(1L)).thenReturn(Optional.of(termin));

        // Act & Assert
        mockMvc.perform(get("/termini/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pacijent.ime").value("John"))
                .andExpect(jsonPath("$.osoblje.ime").value("Dr. Smith"))
                .andExpect(jsonPath("$.obavijest.sadrzaj").value("Checkup"))
                .andExpect(jsonPath("$.status").value("Scheduled"))
                .andExpect(jsonPath("$.datumVrijeme").value("2025-04-01T09:00:00"))
                .andExpect(jsonPath("$.meet_link").value("http://example.com/meet"));
    }

    @Test
    void testReplaceTermin() throws Exception {
        // Arrange
        Korisnik pacijent = new Korisnik();
        pacijent.setIme("John");
        pacijent.setPrezime("Doe");
        pacijent.setEmail("john.doe@example.com");
        pacijent.setKorisnikUuid(UUID.randomUUID());

        Korisnik osoblje = new Korisnik();
        osoblje.setIme("Dr. Smith");
        osoblje.setPrezime("Johnson");
        osoblje.setEmail("dr.smith@example.com");
        osoblje.setKorisnikUuid(UUID.randomUUID());

        Obavijest obavijest = new Obavijest();
        obavijest.setSadrzaj("Checkup");

        Termin existingTermin = new Termin();
        existingTermin.setId(1L);
        existingTermin.setPacijent(pacijent);
        existingTermin.setOsoblje(osoblje);
        existingTermin.setObavijest(obavijest);
        existingTermin.setStatus("Scheduled");
        existingTermin.setDatumVrijeme(LocalDateTime.parse("2025-04-01T09:00:00"));
        existingTermin.setTrajanje(30);
        existingTermin.setMeet_link("http://example.com/meet");

        Termin updatedTermin = new Termin();
        updatedTermin.setPacijent(pacijent);
        updatedTermin.setOsoblje(osoblje);
        updatedTermin.setObavijest(obavijest);
        updatedTermin.setStatus("Completed");
        updatedTermin.setDatumVrijeme(LocalDateTime.parse("2025-04-01T10:00:00"));
        updatedTermin.setTrajanje(30);
        updatedTermin.setMeet_link("http://example.com/meet");

        when(terminService.findById(1L)).thenReturn(Optional.of(existingTermin));
        when(terminService.saveTermin(any(Termin.class))).thenReturn(updatedTermin);

        // Act & Assert
        mockMvc.perform(put("/termini/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pacijent\":{\"ime\":\"John\",\"prezime\":\"Doe\",\"email\":\"john.doe@example.com\",\"korisnikUuid\":\"" + pacijent.getKorisnikUuid() + "\"}, \"osoblje\":{\"ime\":\"Dr. Smith\",\"prezime\":\"Johnson\",\"email\":\"dr.smith@example.com\",\"korisnikUuid\":\"" + osoblje.getKorisnikUuid() + "\"}, \"obavijest\":{\"sadrzaj\":\"Checkup\"}, \"status\":\"Completed\", \"datumVrijeme\":\"2025-04-01T10:00:00\", \"trajanjes\":30, \"meet_link\":\"http://example.com/meet\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("Completed"))
                .andExpect(jsonPath("$.datumVrijeme").value("2025-04-01T10:00:00"));
    }

    @Test
    void testDeleteTermin() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/termini/1"))
                .andExpect(status().isNoContent());

        verify(terminService, times(1)).deleteById(1L);
    }
}
