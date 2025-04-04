package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.service.FakturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FakturaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FakturaService fakturaService;

    @InjectMocks
    private FakturaController fakturaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fakturaController).build();
    }

    @Test
    public void testGetAllFakture() throws Exception {
        Faktura faktura1 = new Faktura();
        faktura1.setId(1L);
        faktura1.setIznos(100.0);
        faktura1.setStatus("Neplaceno");
        faktura1.setMetod("Gotovina");

        Faktura faktura2 = new Faktura();
        faktura2.setId(2L);
        faktura2.setIznos(200.0);
        faktura2.setStatus("Placeno");
        faktura2.setMetod("Kartica");

        when(fakturaService.getAllFakture()).thenReturn(Arrays.asList(faktura1, faktura2));

        mockMvc.perform(get("/fakture"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].iznos").value(100.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].status").value("Placeno"));

        verify(fakturaService, times(1)).getAllFakture();
    }

    @Test
    public void testCreateFaktura() throws Exception {
        Faktura faktura = new Faktura();
        faktura.setId(1L);
        faktura.setIznos(150.0);
        faktura.setStatus("Neplaceno");
        faktura.setMetod("Debit");

        when(fakturaService.saveFaktura(any(Faktura.class))).thenReturn(faktura);

        mockMvc.perform(post("/fakture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(faktura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.iznos").value(150.0));

        verify(fakturaService, times(1)).saveFaktura(any(Faktura.class));
    }

    @Test
    public void testGetFakturaById() throws Exception {
        Faktura faktura = new Faktura();
        faktura.setId(1L);
        faktura.setIznos(100.0);
        faktura.setStatus("Neplaceno");
        faktura.setMetod("Gotovina");

        when(fakturaService.findById(1L)).thenReturn(Optional.of(faktura));

        mockMvc.perform(get("/fakture/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.iznos").value(100.0));

        verify(fakturaService, times(1)).findById(1L);
    }

    @Test
    public void testGetFakturaById_NotFound() throws Exception {
        when(fakturaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/fakture/1"))
                .andExpect(status().isNotFound());

        verify(fakturaService, times(1)).findById(1L);
    }

    @Test
    public void testReplaceFaktura() throws Exception {
        Faktura existingFaktura = new Faktura();
        existingFaktura.setId(1L);
        existingFaktura.setIznos(100.0);
        existingFaktura.setStatus("Neplaceno");
        existingFaktura.setMetod("Gotovina");

        Faktura updatedFaktura = new Faktura();
        updatedFaktura.setId(1L);
        updatedFaktura.setIznos(200.0);
        updatedFaktura.setStatus("Placeno");
        updatedFaktura.setMetod("Kartica");

        when(fakturaService.findById(1L)).thenReturn(Optional.of(existingFaktura));
        when(fakturaService.saveFaktura(any(Faktura.class))).thenReturn(updatedFaktura);

        mockMvc.perform(put("/fakture/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedFaktura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iznos").value(200.0))
                .andExpect(jsonPath("$.status").value("Placeno"));

        verify(fakturaService, times(1)).findById(1L);
        verify(fakturaService, times(1)).saveFaktura(any(Faktura.class));
    }

    @Test
    public void testDeleteFaktura() throws Exception {
        doNothing().when(fakturaService).deleteById(1L);

        mockMvc.perform(delete("/fakture/1"))
                .andExpect(status().isOk());

        verify(fakturaService, times(1)).deleteById(1L);
    }
}
