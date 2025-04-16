package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Servis.KartonServis;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class KartonKontrolerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Koristite @MockBean za mockiranje bean-ova u Spring kontekstu
    private KartonServis kartonServis;

    @Autowired
    private ObjectMapper objectMapper; // Spring Boot će automatski konfigurirati ObjectMapper za HATEOAS

    @BeforeEach
    public void setUp() {
        // MockitoAnnotations.openMocks(this); // Nije potrebno kada se koristi @SpringBootTest i @MockBean
        // objectMapper = new ObjectMapper(); // Spring Boot se brine o ovome
        // objectMapper.registerModule(new JavaTimeModule()); // Spring Boot bi trebao automatski registrirati JavaTimeModule
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

        mockMvc.perform(get("/kartoni").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.kartonList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.kartonList[0].brojKartona").value("K123"))
                .andExpect(jsonPath("$._embedded.kartonList[1].id").value(2))
                .andExpect(jsonPath("$._embedded.kartonList[1].brojKartona").value("K456"))
                .andExpect(jsonPath("$._links.self.href").exists());

        verify(kartonServis, times(1)).getAllKartoni();
    }

    @Test
    public void testCreateKarton() throws Exception {
        Karton karton = new Karton();
        karton.setPacijentUuid(1);
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
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(karton)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brojKartona").value("K789"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.kartoni.href").exists());

        verify(kartonServis, times(1)).saveKarton(any(Karton.class));
    }

    @Test
    public void testGetKartonById() throws Exception {
        Karton karton = new Karton();
        karton.setId(1);
        karton.setPacijentUuid(1);
        karton.setDatumKreiranja(LocalDateTime.now());
        karton.setBrojKartona("K123");

        when(kartonServis.findById(1)).thenReturn(Optional.of(karton));

        mockMvc.perform(get("/kartoni/1").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brojKartona").value("K123"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.kartoni.href").exists());

        verify(kartonServis, times(1)).findById(1);
    }

    @Test
    public void testGetKartonById_NotFound() throws Exception {
        when(kartonServis.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/kartoni/1").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());

        verify(kartonServis, times(1)).findById(1);
    }

    @Test
    public void testReplaceKarton() throws Exception {
        Karton existingKarton = new Karton();
        existingKarton.setId(1);
        existingKarton.setPacijentUuid(1);
        existingKarton.setDatumKreiranja(LocalDateTime.now());
        existingKarton.setBrojKartona("K123");

        Karton updatedKarton = new Karton();
        updatedKarton.setId(1);
        updatedKarton.setPacijentUuid(2);
        updatedKarton.setDatumKreiranja(LocalDateTime.now().plusDays(1));
        updatedKarton.setBrojKartona("K456");

        when(kartonServis.updateKarton(eq(1), any(Karton.class))).thenReturn(Optional.of(updatedKarton));

        mockMvc.perform(put("/kartoni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(updatedKarton)))
                .andExpect(status().isOk()) // PUT vraća OK (200)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.brojKartona").value("K456"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.kartoni.href").exists());

        verify(kartonServis, times(1)).updateKarton(eq(1), any(Karton.class));
    }

    @Test
    public void testReplaceKarton_NotFound() throws Exception {
        Karton updatedKarton = new Karton();
        updatedKarton.setPacijentUuid(1);
        updatedKarton.setDatumKreiranja(LocalDateTime.now().plusDays(1));
        updatedKarton.setBrojKartona("K456");

        when(kartonServis.updateKarton(eq(1), any(Karton.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/kartoni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(updatedKarton)))
                .andExpect(status().isNotFound());

        verify(kartonServis, times(1)).updateKarton(eq(1), any(Karton.class));
        verify(kartonServis, never()).saveKarton(any(Karton.class));
    }

    @Test
    public void testDeleteKarton() throws Exception {
        when(kartonServis.deleteById(1)).thenReturn(true);

        mockMvc.perform(delete("/kartoni/1").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNoContent());

        verify(kartonServis, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteKarton_NotFound() throws Exception {
        when(kartonServis.deleteById(1)).thenReturn(false);

        mockMvc.perform(delete("/kartoni/1").accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound());

        verify(kartonServis, times(1)).deleteById(1);
    }
}