package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.SobaNotFoundException;
import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.service.SobaService;
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

class SobaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SobaService sobaService;

    @InjectMocks
    private SobaController sobaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sobaController).build();
    }

    @Test
    void testGetAllSobe() throws Exception {
        // Arrange
        Soba soba = new Soba();
        soba.setId(1L);
        soba.setBroj_sobe("101");
        soba.setStatus("Available");
        soba.setKapacitet(2);
        soba.setKorisnik(null); // Assume no user for simplicity

        when(sobaService.getAllSobe()).thenReturn(Collections.singletonList(soba));

        // Act & Assert
        mockMvc.perform(get("/sobe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].broj_sobe").value("101"))
                .andExpect(jsonPath("$[0].status").value("Available"))
                .andExpect(jsonPath("$[0].kapacitet").value(2));
    }

    @Test
    void testCreateSoba() throws Exception {
        // Arrange
        Soba soba = new Soba();
        soba.setId(1L);
        soba.setBroj_sobe("101");
        soba.setStatus("Available");
        soba.setKapacitet(2);
        soba.setKorisnik(null); // Assume no user for simplicity

        when(sobaService.saveSoba(any(Soba.class))).thenReturn(soba);

        // Act & Assert
        mockMvc.perform(post("/sobe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"broj_sobe\":\"101\", \"status\":\"Available\", \"kapacitet\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.broj_sobe").value("101"))
                .andExpect(jsonPath("$.status").value("Available"))
                .andExpect(jsonPath("$.kapacitet").value(2));
    }

    @Test
    void testGetSobaById() throws Exception {
        // Arrange
        Soba soba = new Soba();
        soba.setId(1L);
        soba.setBroj_sobe("101");
        soba.setStatus("Available");
        soba.setKapacitet(2);
        soba.setKorisnik(null); // Assume no user for simplicity

        when(sobaService.findById(1L)).thenReturn(Optional.of(soba));

        // Act & Assert
        mockMvc.perform(get("/sobe/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.broj_sobe").value("101"))
                .andExpect(jsonPath("$.status").value("Available"))
                .andExpect(jsonPath("$.kapacitet").value(2));
    }

    @Test
    void testGetSobaByIdNotFound() throws Exception {
        // Arrange
        when(sobaService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/sobe/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Soba not found"))
                .andExpect(jsonPath("$.message").value("Soba sa ID 1 nije pronađena."));
    }

    @Test
    void testReplaceSoba() throws Exception {
        // Arrange
        Soba existingSoba = new Soba();
        existingSoba.setId(1L);
        existingSoba.setBroj_sobe("101");
        existingSoba.setStatus("Available");
        existingSoba.setKapacitet(2);
        existingSoba.setKorisnik(null); // Assume no user for simplicity

        Soba updatedSoba = new Soba();
        updatedSoba.setBroj_sobe("102");
        updatedSoba.setStatus("Occupied");
        updatedSoba.setKapacitet(3);
        updatedSoba.setKorisnik(null); // Assume no user for simplicity

        when(sobaService.findById(1L)).thenReturn(Optional.of(existingSoba));
        when(sobaService.saveSoba(any(Soba.class))).thenReturn(updatedSoba);

        // Act & Assert
        mockMvc.perform(put("/sobe/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"broj_sobe\":\"102\", \"status\":\"Occupied\", \"kapacitet\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.broj_sobe").value("102"))
                .andExpect(jsonPath("$.status").value("Occupied"))
                .andExpect(jsonPath("$.kapacitet").value(3));
    }

    @Test
    void testDeleteSoba() throws Exception {
        // Act & Assert
        doNothing().when(sobaService).deleteById(1L);

        mockMvc.perform(delete("/sobe/1"))
                .andExpect(status().isOk());

        verify(sobaService, times(1)).deleteById(1L);
    }
}