package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Exception.KartonNotFoundException;
import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
import com.example.elektronski_karton_servis.Servis.KartonServis;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class KartonKontrolerTest {

    private MockMvc mockMvc;

    @Mock
    private KartonServis kartonServis; // Mockirajte servis

    @InjectMocks
    private KartonKontroler kartonKontroler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(kartonKontroler).build();
    }

    @Test
    public void testGetAllKartoni() throws Exception {
        Karton karton1 = new Karton();
        karton1.setId(1);
        karton1.setPacijentUuid(123);
        karton1.setDatumKreiranja(LocalDateTime.now());
        karton1.setBrojKartona("K123");

        Karton karton2 = new Karton();
        karton2.setId(2);
        karton2.setPacijentUuid(456);
        karton2.setDatumKreiranja(LocalDateTime.now().minusDays(1));
        karton2.setBrojKartona("K456");

        when(kartonServis.getAllKartoni()).thenReturn(Arrays.asList(karton1, karton2));

        mockMvc.perform(get("/kartoni"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].brojKartona").value("K123"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].brojKartona").value("K456"));

        verify(kartonServis, times(1)).getAllKartoni();
    }

    @Test
    public void testCreateKarton() throws Exception {
        Karton karton = new Karton();
        karton.setPacijentUuid(1); // Koristite Integer
        karton.setDatumKreiranja(LocalDateTime.now());
        karton.setBrojKartona("K789");

        Karton savedKarton = new Karton();
        savedKarton.setId(1);
        savedKarton.setPacijentUuid(karton.getPacijentUuid());
        savedKarton.setDatumKreiranja(karton.getDatumKreiranja());
        savedKarton.setBrojKartona(karton.getBrojKartona());

        when(kartonServis.saveKarton(any(Karton.class))).thenReturn(savedKarton);

        mockMvc.perform(post("/kartoni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(karton)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brojKartona").value("K789"));

        verify(kartonServis, times(1)).saveKarton(any(Karton.class));
    }

    @Test
    public void testGetKartonById() throws Exception {
        Karton karton = new Karton();
        karton.setId(1);
        karton.setPacijentUuid(1); // Koristite Integer
        karton.setDatumKreiranja(LocalDateTime.now());
        karton.setBrojKartona("K123");

        when(kartonServis.findById(1)).thenReturn(Optional.of(karton));

        mockMvc.perform(get("/kartoni/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brojKartona").value("K123"));

        verify(kartonServis, times(1)).findById(1);
    }

    @Test
    public void testGetKartonById_NotFound() throws Exception {
        when(kartonServis.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/kartoni/1"))
                .andExpect(status().isNotFound());

        verify(kartonServis, times(1)).findById(1);
    }

    @Test
    public void testReplaceKarton() throws Exception {
        Karton existingKarton = new Karton();
        existingKarton.setId(1);
        existingKarton.setPacijentUuid(1); // Koristite Integer
        existingKarton.setDatumKreiranja(LocalDateTime.now());
        existingKarton.setBrojKartona("K123");

        Karton updatedKarton = new Karton();
        updatedKarton.setPacijentUuid(2); // Koristite Integer
        updatedKarton.setDatumKreiranja(LocalDateTime.now().plusDays(1));
        updatedKarton.setBrojKartona("K456");

        when(kartonServis.findById(1)).thenReturn(Optional.of(existingKarton));
        when(kartonServis.saveKarton(any(Karton.class))).thenReturn(updatedKarton);

        mockMvc.perform(put("/kartoni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedKarton)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brojKartona").value("K456"));

        verify(kartonServis, times(1)).findById(1);
        verify(kartonServis, times(1)).saveKarton(any(Karton.class));
    }

    @Test
    public void testReplaceKarton_NotFound() throws Exception {
        Karton updatedKarton = new Karton();
        updatedKarton.setPacijentUuid(1); // Koristite Integer
        updatedKarton.setDatumKreiranja(LocalDateTime.now().plusDays(1));
        updatedKarton.setBrojKartona("K456");

        when(kartonServis.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/kartoni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedKarton)))
                .andExpect(status().isNotFound());

        verify(kartonServis, times(1)).findById(1);
        verify(kartonServis, never()).saveKarton(any(Karton.class));
    }

    @Test
    public void testDeleteKarton() throws Exception {
        when(kartonServis.findById(1)).thenReturn(Optional.of(new Karton()));
        doNothing().when(kartonServis).deleteById(1);

        mockMvc.perform(delete("/kartoni/1"))
                .andExpect(status().isNoContent());

        verify(kartonServis, times(1)).findById(1);
        verify(kartonServis, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteKarton_NotFound() throws Exception {
        when(kartonServis.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/kartoni/1"))
                .andExpect(status().isNotFound());

        verify(kartonServis, times(1)).findById(1);
        verify(kartonServis, never()).deleteById(1);
    }
}