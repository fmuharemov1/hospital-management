package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Controller.KartonKontroler;
import com.example.elektronski_karton_servis.Servis.KartonServis;
import com.example.elektronski_karton_servis.model.Karton;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KartonKontroler.class)
@Import(KartonKontrolerTest.TestConfig.class)
public class KartonKontrolerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private KartonServis kartonServis;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public KartonServis kartonServis() {
            return mock(KartonServis.class);
        }
    }

    private Karton sample() {
        Karton k = new Karton();
        k.setId(1);
        k.setBrojKartona("BK001");
        k.setDatumKreiranja(LocalDate.now().atStartOfDay());
        k.setPacijentUuid((1));
        return k;
    }

    @Test
    void all() throws Exception {
        when(kartonServis.getAllKartoni()).thenReturn(List.of(sample()));
        mockMvc.perform(get("/kartoni")).andExpect(status().isOk());
    }

    @Test
    void one_found() throws Exception {
        when(kartonServis.findById(1)).thenReturn(Optional.of(sample()));
        mockMvc.perform(get("/kartoni/1")).andExpect(status().isOk());
    }

    @Test
    void one_not_found() throws Exception {
        when(kartonServis.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(get("/kartoni/1")).andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Karton k = sample();
        when(kartonServis.saveKarton(any())).thenReturn(k);

        mockMvc.perform(post("/kartoni")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k)))
                .andExpect(status().isCreated());
    }

    @Test
    void update_found() throws Exception {
        Karton k = sample();
        when(kartonServis.updateKarton(eq(1), any())).thenReturn(Optional.of(k));

        mockMvc.perform(put("/kartoni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(k)))
                .andExpect(status().isOk());
    }

    @Test
    void update_not_found() throws Exception {
        when(kartonServis.updateKarton(eq(1), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/kartoni/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_found() throws Exception {
        when(kartonServis.deleteById(1)).thenReturn(true);
        mockMvc.perform(delete("/kartoni/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_not_found() throws Exception {
        when(kartonServis.deleteById(1)).thenReturn(false);
        mockMvc.perform(delete("/kartoni/1")).andExpect(status().isNotFound());
    }
}