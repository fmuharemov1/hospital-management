package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Controller.DijagnozaKontroler;
import com.example.elektronski_karton_servis.Repository.DijagnozaRepository;
import com.example.elektronski_karton_servis.Servis.DijagnozaServis;
import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DijagnozaKontroler.class)
@Import(DijagnozaKontrolerTest.TestConfig.class)
public class DijagnozaKontrolerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private DijagnozaRepository repo;

    @TestConfiguration
    static class TestConfig {
        @Bean public DijagnozaRepository repo() { return mock(DijagnozaRepository.class); }
        @Bean public DijagnozaServis servis() { return mock(DijagnozaServis.class); }
    }

    private Dijagnoza sample() {
        Dijagnoza d = new Dijagnoza();
        d.setId(1);
        d.setNaziv("Gripa");
        d.setOpis("Simptomi prehlade");
        d.setDatumDijagnoze(LocalDateTime.now());
        d.setKartonId(10);
        d.setOsobljeUuid((1));
        return d;
    }

    @Test
    void all() throws Exception {
        when(repo.findAll()).thenReturn(List.of(sample()));
        mockMvc.perform(get("/dijagnoze")).andExpect(status().isOk());
    }

    @Test
    void one_found() throws Exception {
        when(repo.findById(1)).thenReturn(Optional.of(sample()));
        mockMvc.perform(get("/dijagnoze/1")).andExpect(status().isOk());
    }

    @Test
    void one_not_found() throws Exception {
        when(repo.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(get("/dijagnoze/1")).andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Dijagnoza d = sample();
        when(repo.save(any())).thenReturn(d);

        mockMvc.perform(post("/dijagnoze")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isCreated());
    }

    @Test
    void update_found() throws Exception {
        Dijagnoza d = sample();
        when(repo.findById(1)).thenReturn(Optional.of(d));
        when(repo.save(any())).thenReturn(d);

        mockMvc.perform(put("/dijagnoze/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(d)))
                .andExpect(status().isOk());
    }

    @Test
    void update_not_found() throws Exception {
        when(repo.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/dijagnoze/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_found() throws Exception {
        when(repo.existsById(1)).thenReturn(true);
        doNothing().when(repo).deleteById(1);
        mockMvc.perform(delete("/dijagnoze/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_not_found() throws Exception {
        when(repo.existsById(1)).thenReturn(false);
        mockMvc.perform(delete("/dijagnoze/1")).andExpect(status().isNotFound());
    }
}