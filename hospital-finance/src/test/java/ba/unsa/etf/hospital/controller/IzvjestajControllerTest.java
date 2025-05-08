package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.service.IzvjestajService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IzvjestajController.class)
public class IzvjestajControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IzvjestajService izvjestajService;

    @Autowired
    private ObjectMapper objectMapper;

    private Izvjestaj izvjestaj;

    @BeforeEach
    public void setUp() {
        izvjestaj = new Izvjestaj();
        izvjestaj.setId(1L);
        izvjestaj.setTipIzvjestaja("Mjesečni");
        izvjestaj.setBrojPacijenata(10);
        izvjestaj.setBrojTermina(5);
        izvjestaj.setFinansijskiPregled(2000.0);
        izvjestaj.setDatumGenerisanja(new Date());
    }

    @Test
    public void testGetAllIzvjestaji() throws Exception {
        Page<Izvjestaj> page = new PageImpl<>(Collections.singletonList(izvjestaj));
        when(izvjestajService.getIzvjestajiSortedByFinansijskiPregled(0, 10)).thenReturn(page);

        mockMvc.perform(get("/izvjestaji"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tipIzvjestaja").value("Mjesečni"));

        verify(izvjestajService, times(1)).getIzvjestajiSortedByFinansijskiPregled(0, 10);
    }

    @Test
    public void testFilterByTipAndMinPacijenti() throws Exception {
        when(izvjestajService.getIzvjestajiByTipAndMinPacijenti("Mjesečni", 5))
                .thenReturn(Collections.singletonList(izvjestaj));

        mockMvc.perform(get("/izvjestaji/filter")
                        .param("tipIzvjestaja", "Mjesečni")
                        .param("minBrojPacijenata", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brojPacijenata").value(10));
    }

    @Test
    public void testGetOneById() throws Exception {
        when(izvjestajService.findById(1L)).thenReturn(Optional.of(izvjestaj));

        mockMvc.perform(get("/izvjestaji/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipIzvjestaja").value("Mjesečni"));
    }

    @Test
    public void testCreateIzvjestaj() throws Exception {
        when(izvjestajService.saveIzvjestaj(any(Izvjestaj.class))).thenReturn(izvjestaj);

        mockMvc.perform(post("/izvjestaji")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(izvjestaj)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipIzvjestaja").value("Mjesečni"));
    }

    @Test
    public void testUpdateIzvjestaj() throws Exception {
        when(izvjestajService.findById(1L)).thenReturn(Optional.of(izvjestaj));
        when(izvjestajService.saveIzvjestaj(any(Izvjestaj.class))).thenReturn(izvjestaj);

        mockMvc.perform(put("/izvjestaji/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(izvjestaj)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brojTermina").value(5));
    }

    @Test
    public void testDeleteIzvjestaj() throws Exception {
        doNothing().when(izvjestajService).deleteById(1L);

        mockMvc.perform(delete("/izvjestaji/1"))
                .andExpect(status().isOk());

        verify(izvjestajService, times(1)).deleteById(1L);
    }

    @Test
    public void testBatchSave() throws Exception {
        List<Izvjestaj> lista = Arrays.asList(izvjestaj);
        when(izvjestajService.saveBatchIzvjestaji(anyList())).thenReturn(lista);

        mockMvc.perform(post("/izvjestaji/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lista)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipIzvjestaja").value("Mjesečni"));
    }
}
