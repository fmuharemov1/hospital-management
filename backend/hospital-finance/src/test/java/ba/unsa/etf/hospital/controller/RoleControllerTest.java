package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.RoleNotFoundException;
import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllRoles() throws Exception {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setTipKorisnika("Pacijent");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setTipKorisnika("Doktor");

        when(roleService.getAllRoles()).thenReturn(Arrays.asList(role1, role2));

        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetRoleById_Found() throws Exception {
        Role role = new Role();
        role.setId(1L);
        role.setTipKorisnika("Pacijent");

        when(roleService.findById(1L)).thenReturn(Optional.of(role));

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipKorisnika").value("Pacijent"));
    }

    @Test
    public void testGetRoleById_NotFound() throws Exception {
        when(roleService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateRole() throws Exception {
        Role role = new Role();
        role.setTipKorisnika("Pacijent");

        when(roleService.saveRole(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipKorisnika").value("Pacijent"));
    }

    @Test
    public void testReplaceRole_Found() throws Exception {
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setTipKorisnika("Pacijent");

        Role newRole = new Role();
        newRole.setId(1L);
        newRole.setTipKorisnika("Doktor");
        newRole.setSmjena("Jutarnja");
        newRole.setOdjeljenje("Hirurgija");

        when(roleService.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleService.saveRole(any(Role.class))).thenReturn(newRole);

        mockMvc.perform(put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipKorisnika").value("Doktor"));
    }

    @Test
    public void testReplaceRole_NotFound() throws Exception {
        Role newRole = new Role();
        newRole.setId(1L);
        newRole.setTipKorisnika("Doktor");

        when(roleService.findById(1L)).thenReturn(Optional.empty());
        when(roleService.saveRole(any(Role.class))).thenReturn(newRole);

        mockMvc.perform(put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipKorisnika").value("Doktor"));
    }

    @Test
    public void testDeleteRole_Success() throws Exception {
        doNothing().when(roleService).deleteById(1L);

        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteRole_NotFound() throws Exception {
        doThrow(new RoleNotFoundException(1L)).when(roleService).deleteById(1L);

        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().isNotFound());
    }
}
