package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Controller.RoleKontroler;
import com.example.elektronski_karton_servis.Repository.RoleRepository;
import com.example.elektronski_karton_servis.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ActiveProfiles("test")

@WebMvcTest(RoleKontroler.class)
@Import(RoleKontrolerTest.TestConfig.class)
public class RoleKontrolerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RoleRepository roleRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean public RoleRepository roleRepository() {
            return mock(RoleRepository.class);
        }
    }

    private Role sample() {
        Role r = new Role();
        r.setId(1);
        r.setTipKorisnika("Doktor");
        r.setSmjena("Jutarnja");
        r.setOdjeljenje("Interno");
        return r;
    }

    @Test
    void all() throws Exception {
        when(roleRepository.findAll()).thenReturn(List.of(sample()));
        mockMvc.perform(get("/roles")).andExpect(status().isOk());
    }

    @Test
    void one_found() throws Exception {
        when(roleRepository.findById(1)).thenReturn(Optional.of(sample()));
        mockMvc.perform(get("/roles/1")).andExpect(status().isOk());
    }

    @Test
    void one_not_found() throws Exception {
        when(roleRepository.findById(1)).thenReturn(Optional.empty());
        mockMvc.perform(get("/roles/1")).andExpect(status().isNotFound());
    }

    @Test
    void create() throws Exception {
        Role r = sample();
        when(roleRepository.save(any())).thenReturn(r);

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isCreated());
    }

    @Test
    void update_found() throws Exception {
        Role r = sample();
        when(roleRepository.findById(1)).thenReturn(Optional.of(r));
        when(roleRepository.save(any())).thenReturn(r);

        mockMvc.perform(put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk());
    }

    @Test
    void update_not_found() throws Exception {
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sample())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_found_success() throws Exception {
        when(roleRepository.existsById(1)).thenReturn(true);
        doNothing().when(roleRepository).deleteById(1);
        mockMvc.perform(delete("/roles/1")).andExpect(status().isNoContent());
    }

    @Test
    void delete_found_conflict() throws Exception {
        when(roleRepository.existsById(1)).thenReturn(true);
        doThrow(new DataIntegrityViolationException("")).when(roleRepository).deleteById(1);

        mockMvc.perform(delete("/roles/1")).andExpect(status().isConflict());
    }

    @Test
    void delete_not_found() throws Exception {
        when(roleRepository.existsById(1)).thenReturn(false);
        mockMvc.perform(delete("/roles/1")).andExpect(status().isNotFound());
    }
}