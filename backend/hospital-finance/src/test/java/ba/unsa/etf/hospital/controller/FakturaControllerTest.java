package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.service.FakturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FakturaController.class)
public class FakturaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FakturaService fakturaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Faktura faktura;

    @BeforeEach
    public void setUp() {
        faktura = new Faktura();
        faktura.setId(1L);
        faktura.setIznos(150.0);
        faktura.setStatus("Neplaćeno");
        faktura.setMetod("Gotovina");
    }

    @Test
    public void testGetAllFakture() throws Exception {
        List<Faktura> fakture = Collections.singletonList(faktura);
        when(fakturaService.getAllFakture()).thenReturn(fakture);

        mockMvc.perform(get("/fakture"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Neplaćeno"));

        verify(fakturaService, times(1)).getAllFakture();
    }

    @Test
    public void testCreateFaktura() throws Exception {
        when(fakturaService.saveFaktura(any(Faktura.class))).thenReturn(faktura);

        mockMvc.perform(post("/fakture")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faktura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Neplaćeno"));

        verify(fakturaService, times(1)).saveFaktura(any(Faktura.class));
    }

    @Test
    public void testGetOneFaktura() throws Exception {
        when(fakturaService.findById(1L)).thenReturn(Optional.of(faktura));

        mockMvc.perform(get("/fakture/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Neplaćeno"));

        verify(fakturaService, times(1)).findById(1L);
    }

    @Test
    public void testGetOneFakturaNotFound() throws Exception {
        when(fakturaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/fakture/1"))
                .andExpect(status().isNotFound());

        verify(fakturaService, times(1)).findById(1L);
    }

    @Test
    public void testUpdateFaktura() throws Exception {
        Faktura updatedFaktura = new Faktura();
        updatedFaktura.setIznos(200.0);
        updatedFaktura.setStatus("Plaćeno");
        updatedFaktura.setMetod("Kartica");

        when(fakturaService.findById(1L)).thenReturn(Optional.of(faktura));
        when(fakturaService.saveFaktura(any(Faktura.class))).thenReturn(updatedFaktura);

        mockMvc.perform(put("/fakture/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFaktura)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Plaćeno"));

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

    @Test
    public void testDeleteFakturaNotFound() throws Exception {
        Long id = 1L;

        // Simuliramo da deleteById baca FakturaNotFoundException
        doThrow(new FakturaNotFoundException(id)).when(fakturaService).deleteById(id);

        mockMvc.perform(delete("/fakture/{id}", id))
                .andExpect(status().isNotFound()); // Očekujemo 404 jer je faktura nepostojeća

        verify(fakturaService, times(1)).deleteById(id);
    }



    @Test
    public void testBatchSaveFakture() throws Exception {
        List<Faktura> faktureBatch = Collections.singletonList(faktura);
        when(fakturaService.saveBatchFakture(anyList())).thenReturn(faktureBatch);

        mockMvc.perform(post("/fakture/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faktureBatch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("Neplaćeno"));

        verify(fakturaService, times(1)).saveBatchFakture(anyList());
    }
}
