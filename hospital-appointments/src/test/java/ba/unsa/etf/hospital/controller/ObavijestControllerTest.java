package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.service.ObavijestService;
import ba.unsa.etf.hospital.exception.ObavijestNotFoundException;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ObavijestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ObavijestService obavijestService;

    @InjectMocks
    private ObavijestController obavijestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(obavijestController).build();
    }

    @Test
    void testGetAllObavijesti() throws Exception {
        // Arrange
        Obavijest obavijest = new Obavijest();
        obavijest.setId(1L);
        obavijest.setSadrzaj("Test content");
        obavijest.setDatum_vrijeme(LocalDateTime.of(2025, 4, 10, 0, 0));

        when(obavijestService.getAllObavijesti()).thenReturn(Collections.singletonList(obavijest));

        // Act & Assert
        mockMvc.perform(get("/obavijesti"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sadrzaj").value("Test content"))
                .andExpect(jsonPath("$[0].datum_vrijeme[0]").value(2025))
                .andExpect(jsonPath("$[0].datum_vrijeme[1]").value(4))
                .andExpect(jsonPath("$[0].datum_vrijeme[2]").value(10))
                .andExpect(jsonPath("$[0].datum_vrijeme[3]").value(0))
                .andExpect(jsonPath("$[0].datum_vrijeme[4]").value(0));
    }

    @Test
    void testCreateObavijest() throws Exception {
        // Arrange
        Obavijest obavijest = new Obavijest();
        obavijest.setId(1L);
        obavijest.setSadrzaj("Test content");
        obavijest.setDatum_vrijeme(LocalDateTime.of(2025, 4, 10, 0, 0));

        when(obavijestService.saveObavijest(any(Obavijest.class))).thenReturn(obavijest);

        // Act & Assert
        mockMvc.perform(post("/obavijesti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sadrzaj\":\"Test content\", \"datum_vrijeme\":\"2025-04-10T00:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sadrzaj").value("Test content"))
                .andExpect(jsonPath("$.datum_vrijeme[0]").value(2025))
                .andExpect(jsonPath("$.datum_vrijeme[1]").value(4))
                .andExpect(jsonPath("$.datum_vrijeme[2]").value(10))
                .andExpect(jsonPath("$.datum_vrijeme[3]").value(0))
                .andExpect(jsonPath("$.datum_vrijeme[4]").value(0));
    }

    @Test
    void testGetObavijestById() throws Exception {
        // Arrange
        Obavijest obavijest = new Obavijest();
        obavijest.setId(1L);
        obavijest.setSadrzaj("Test content");
        obavijest.setDatum_vrijeme(LocalDateTime.of(2025, 4, 10, 0, 0));

        when(obavijestService.findById(1L)).thenReturn(Optional.of(obavijest));

        // Act & Assert
        mockMvc.perform(get("/obavijesti/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sadrzaj").value("Test content"))
                .andExpect(jsonPath("$.datum_vrijeme[0]").value(2025))
                .andExpect(jsonPath("$.datum_vrijeme[1]").value(4))
                .andExpect(jsonPath("$.datum_vrijeme[2]").value(10))
                .andExpect(jsonPath("$.datum_vrijeme[3]").value(0))
                .andExpect(jsonPath("$.datum_vrijeme[4]").value(0));
    }

    @Test
    void testGetObavijestByIdNotFound() throws Exception {
        // Arrange
        when(obavijestService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/obavijesti/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReplaceObavijest() throws Exception {
        // Arrange
        Obavijest existingObavijest = new Obavijest();
        existingObavijest.setId(1L);
        existingObavijest.setSadrzaj("Old content");
        existingObavijest.setDatum_vrijeme(LocalDateTime.of(2025, 4, 1, 0, 0));

        Obavijest updatedObavijest = new Obavijest();
        updatedObavijest.setSadrzaj("Updated content");
        updatedObavijest.setDatum_vrijeme(LocalDateTime.of(2025, 4, 10, 0, 0));

        when(obavijestService.findById(1L)).thenReturn(Optional.of(existingObavijest));
        when(obavijestService.saveObavijest(any(Obavijest.class))).thenReturn(updatedObavijest);

        // Act & Assert
        mockMvc.perform(put("/obavijesti/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sadrzaj\":\"Updated content\", \"datum_vrijeme\":\"2025-04-10T00:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sadrzaj").value("Updated content"))
                .andExpect(jsonPath("$.datum_vrijeme[0]").value(2025))  // Year
                .andExpect(jsonPath("$.datum_vrijeme[1]").value(4))     // Month
                .andExpect(jsonPath("$.datum_vrijeme[2]").value(10))    // Day
                .andExpect(jsonPath("$.datum_vrijeme[3]").value(0))     // Hour
                .andExpect(jsonPath("$.datum_vrijeme[4]").value(0));    // Minute
    }

    @Test
    void testDeleteObavijest() throws Exception {
        // Act & Assert
        doNothing().when(obavijestService).deleteById(1L);

        mockMvc.perform(delete("/obavijesti/1"))
                .andExpect(status().isOk());

        verify(obavijestService, times(1)).deleteById(1L);
    }
}
