package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.RoleNotFoundException;
import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RoleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
    }

    @Test
    void testGetAllRoles() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setTipKorisnika("Admin");
        role.setSmjena("Morning");
        role.setOdjeljenje("IT");

        when(roleService.getAllRoles()).thenReturn(Collections.singletonList(role));

        // Act & Assert
        mockMvc.perform(get("/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tipKorisnika").value("Admin"))
                .andExpect(jsonPath("$[0].smjena").value("Morning"))
                .andExpect(jsonPath("$[0].odjeljenje").value("IT"));
    }

    @Test
    void testCreateRole() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setTipKorisnika("Admin");
        role.setSmjena("Morning");
        role.setOdjeljenje("IT");

        when(roleService.saveRole(any(Role.class))).thenReturn(role);

        // Act & Assert
        mockMvc.perform(post("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipKorisnika\":\"Admin\", \"smjena\":\"Morning\", \"odjeljenje\":\"IT\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipKorisnika").value("Admin"))
                .andExpect(jsonPath("$.smjena").value("Morning"))
                .andExpect(jsonPath("$.odjeljenje").value("IT"));
    }

    @Test
    void testGetRoleById() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1L);
        role.setTipKorisnika("Admin");
        role.setSmjena("Morning");
        role.setOdjeljenje("IT");

        when(roleService.findById(1L)).thenReturn(Optional.of(role));

        // Act & Assert
        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.tipKorisnika").value("Admin"))
                .andExpect(jsonPath("$.smjena").value("Morning"))
                .andExpect(jsonPath("$.odjeljenje").value("IT"));
    }

    @Test
    void testGetRoleByIdNotFound() throws Exception {
        // Arrange
        when(roleService.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Role not found"))
                .andExpect(jsonPath("$.message").value("Role sa ID 1 nije pronađena."));
    }

    @Test
    void testReplaceRole() throws Exception {
        // Arrange
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setTipKorisnika("Admin");
        existingRole.setSmjena("Morning");
        existingRole.setOdjeljenje("IT");

        Role updatedRole = new Role();
        updatedRole.setTipKorisnika("Manager");
        updatedRole.setSmjena("Night");
        updatedRole.setOdjeljenje("HR");

        when(roleService.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleService.saveRole(any(Role.class))).thenReturn(updatedRole);

        // Act & Assert
        mockMvc.perform(put("/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tipKorisnika\":\"Manager\", \"smjena\":\"Night\", \"odjeljenje\":\"HR\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipKorisnika").value("Manager"))
                .andExpect(jsonPath("$.smjena").value("Night"))
                .andExpect(jsonPath("$.odjeljenje").value("HR"));
    }

    @Test
    void testDeleteRole() throws Exception {
        // Act & Assert
        doNothing().when(roleService).deleteById(1L);

        mockMvc.perform(delete("/roles/1"))
                .andExpect(status().isOk());

        verify(roleService, times(1)).deleteById(1L);
    }
}
