package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Controller.TerapijaKontroler;
import com.example.elektronski_karton_servis.Repository.TerapijaRepository;
import com.example.elektronski_karton_servis.model.Terapija;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ActiveProfiles("test")

@WebMvcTest(TerapijaKontroler.class)
@Import(TerapijaKontrolerTest.TestConfig.class)
public class TerapijaKontrolerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TerapijaRepository terapijaRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean public TerapijaRepository terapijaRepository() {
            return mock(TerapijaRepository.class);
        }
    }

    private Terapija sample() {
        Terapija t = new Terapija();
        t.setId(1);
        t.setNaziv("Antibiotik");
        t.setOpis("3x dnevno");
        t.setDatumPocetka(LocalDate.now().atStartOfDay());
        t.setDatumZavrsetka(LocalDate.now().plusDays(7).atStartOfDay());
        t.setOsobljeUuid((1));
        return t;
    }

    @Test
    void all() throws Exception {
        when(terapijaRepository.findAll()).thenReturn(List.of(sample()));
        mockMvc.perform(get("/terapije")).andExpect(status().isOk());
    }

    @Test
    void one_found() throws Exception {
        when(terapijaRepository.findById(1)).thenReturn(Optional.of(sample()));
        mockMvc.perform(get("/terapije/1")).andExpect(status().isOk());
    }

    @Test
    void one_not_found() throws Exception {
        when(terapijaRepository.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(get("/terapije/1")).andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Terapija t = sample();
        when(terapijaRepository.save(any())).thenReturn(t);

        mockMvc.perform(post("/terapije")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isCreated());
    }

    @Test
    void update_found() throws Exception {
        Terapija t = sample();
        when(terapijaRepository.findById(1)).thenReturn(Optional.of(t));
        when(terapijaRepository.save(any())).thenReturn(t);

        mockMvc.perform(put("/terapije/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk());
    }

    @Test
    void update_not_found() throws Exception {
        when(terapijaRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/terapije/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_found() throws Exception {
        when(terapijaRepository.existsById(1)).thenReturn(true);
        doNothing().when(terapijaRepository).deleteById(1);
        mockMvc.perform(delete("/terapije/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_not_found() throws Exception {
        when(terapijaRepository.existsById(1)).thenReturn(false);
        mockMvc.perform(delete("/terapije/1")).andExpect(status().isNotFound());
    }
}