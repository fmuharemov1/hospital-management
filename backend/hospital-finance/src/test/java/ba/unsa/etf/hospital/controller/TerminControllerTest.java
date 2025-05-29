package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.RemoteTerminService;
import ba.unsa.etf.hospital.service.TerminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TerminController.class)
public class TerminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TerminService terminService;

    @MockBean
    private RemoteTerminService remoteTerminService;
    @Autowired
    private ObjectMapper objectMapper;

    private Termin createSampleTermin() {
        Termin termin = new Termin();
        termin.setId(1L);
        termin.setTerminUuid(UUID.randomUUID());
        termin.setDatumVrijeme(LocalDateTime.now());

        Korisnik pacijent = new Korisnik();
        pacijent.setId(2L);
        termin.setPacijent(pacijent);

        Korisnik osoblje = new Korisnik();
        osoblje.setId(3L);
        termin.setOsoblje(osoblje);

        Faktura faktura = new Faktura();
        faktura.setId(1L);
        termin.setFaktura(faktura);

        return termin;
    }

    @Test
    public void testGetAllTerminiAsc() throws Exception {
        Termin termin = createSampleTermin();
        Page<Termin> page = new PageImpl<>(Collections.singletonList(termin), PageRequest.of(0, 10), 1);

        when(terminService.getSortedAndPaginatedTermini(0, 10)).thenReturn(page);

        mockMvc.perform(get("/termini?page=0&size=10&sort=asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void testGetAllTerminiDesc() throws Exception {
        Termin termin = createSampleTermin();
        Page<Termin> page = new PageImpl<>(Collections.singletonList(termin), PageRequest.of(0, 10), 1);

        when(terminService.getSortedAndPaginatedTerminiDesc(0, 10)).thenReturn(page);

        mockMvc.perform(get("/termini?page=0&size=10&sort=desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    public void testGetTerminById_Found() throws Exception {
        Termin termin = createSampleTermin();
        when(terminService.findById(1L)).thenReturn(Optional.of(termin));

        mockMvc.perform(get("/termini/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testGetTerminById_NotFound() throws Exception {
        when(terminService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/termini/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTermin() throws Exception {
        Termin termin = createSampleTermin();
        when(terminService.saveTermin(any(Termin.class))).thenReturn(termin);

        mockMvc.perform(post("/termini")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(termin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateTerminWithFaktura() throws Exception {
        Termin termin = createSampleTermin();
        when(terminService.createTerminWithNewFaktura(any(Termin.class))).thenReturn(termin);

        mockMvc.perform(post("/termini/novi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(termin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testReplaceTermin_NotFound() throws Exception {
        Termin newTermin = createSampleTermin();

        when(terminService.findById(1L)).thenReturn(Optional.empty());
        when(terminService.saveTermin(any(Termin.class))).thenReturn(newTermin);

        mockMvc.perform(put("/termini/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newTermin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteTermin_Success() throws Exception {
        doNothing().when(terminService).deleteById(1L);

        mockMvc.perform(delete("/termini/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTermin_NotFound() throws Exception {
        doThrow(new TerminNotFoundException(1L)).when(terminService).deleteById(1L);

        mockMvc.perform(delete("/termini/1"))
                .andExpect(status().isNotFound());
    }
}
