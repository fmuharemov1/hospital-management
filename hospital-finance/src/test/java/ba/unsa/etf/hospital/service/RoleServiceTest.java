package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRoles() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setTipKorisnika("Pacijent");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setTipKorisnika("Doktor");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        // Testiranje metode
        var roles = roleService.getAllRoles();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals(role1.getId(), roles.get(0).getId());
        assertEquals(role2.getId(), roles.get(1).getId());

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testSaveRole() {
        Role role = new Role();
        role.setTipKorisnika("Pacijent");

        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // Testiranje metode
        Role savedRole = roleService.saveRole(role);

        assertNotNull(savedRole);
        assertEquals(role.getTipKorisnika(), savedRole.getTipKorisnika());

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void testFindById() {
        Role role = new Role();
        role.setId(1L);
        role.setTipKorisnika("Pacijent");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Testiranje metode
        Optional<Role> foundRole = roleService.findById(1L);

        assertTrue(foundRole.isPresent());
        assertEquals(role.getId(), foundRole.get().getId());

        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindById_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // Testiranje metode
        Optional<Role> foundRole = roleService.findById(1L);

        assertFalse(foundRole.isPresent());

        verify(roleRepository, times(1)).findById(1L);
    }

    @Test
    public void testDeleteById() {
        doNothing().when(roleRepository).deleteById(1L);

        // Testiranje metode
        roleService.deleteById(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }
}

