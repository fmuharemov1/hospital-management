package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Controller.TerminKontroler;
import com.example.elektronski_karton_servis.Repository.TerminRepository;
import com.example.elektronski_karton_servis.model.Termin;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TerminKontroler.class)
@Import(TerminKontrolerTest.TestConfig.class)
public class TerminKontrolerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TerminRepository terminRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean public TerminRepository terminRepository() {
            return mock(TerminRepository.class);
        }
    }

    private Termin sample() {
        Termin t = new Termin();
        t.setId(1);
       t.setPacijentUuid((1));
        t.setOsobljeUuid((2));
        t.setDatumVrijeme(LocalDateTime.now().plusDays(1));
        return t;
    }

    @Test
    void all() throws Exception {
        when(terminRepository.findAll()).thenReturn(List.of(sample()));
        mockMvc.perform(get("/termini")).andExpect(status().isOk());
    }

    @Test
    void one_found() throws Exception {
        when(terminRepository.findById(1)).thenReturn(Optional.of(sample()));
        mockMvc.perform(get("/termini/1")).andExpect(status().isOk());
    }

    @Test
    void one_not_found() throws Exception {
        when(terminRepository.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(get("/termini/1")).andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Termin t = sample();
        when(terminRepository.save(any())).thenReturn(t);

        mockMvc.perform(post("/termini")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isCreated());
    }

    @Test
    void update_found() throws Exception {
        Termin t = sample();
        when(terminRepository.findById(1)).thenReturn(Optional.of(t));
        when(terminRepository.save(any())).thenReturn(t);

        mockMvc.perform(put("/termini/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk());
    }

    @Test
    void update_not_found() throws Exception {
        when(terminRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/termini/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_found() throws Exception {
        when(terminRepository.existsById(1)).thenReturn(true);
        doNothing().when(terminRepository).deleteById(1);
        mockMvc.perform(delete("/termini/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_not_found() throws Exception {
        when(terminRepository.existsById(1)).thenReturn(false);
        mockMvc.perform(delete("/termini/1")).andExpect(status().isNotFound());
    }
}