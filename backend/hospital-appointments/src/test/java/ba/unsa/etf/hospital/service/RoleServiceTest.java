package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.RoleNotFoundException;
import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup dummy Role object
        role = new Role();
        role.setId(1L);
        role.setTipKorisnika("Doctor");
        role.setSmjena("Morning");
        role.setOdjeljenje("Surgery");
    }

    @Test
    void testGetAllRoles() {
        // Arrange
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

        // Act
        var roles = roleService.getAllRoles();

        // Assert
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals(role.getTipKorisnika(), roles.get(0).getTipKorisnika());
    }

    @Test
    void testSaveRole() {
        // Arrange
        when(roleRepository.save(role)).thenReturn(role);

        // Act
        Role savedRole = roleService.saveRole(role);

        // Assert
        assertNotNull(savedRole);
        assertEquals(role.getTipKorisnika(), savedRole.getTipKorisnika());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testFindById() {
        // Arrange
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // Act
        Optional<Role> foundRole = roleService.findById(1L);

        // Assert
        assertTrue(foundRole.isPresent());
        assertEquals(role.getTipKorisnika(), foundRole.get().getTipKorisnika());
    }

    @Test
    public void testDeleteById_Role() {
        Long id = 1L;

        Role role = new Role();
        role.setId(id);

        when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).deleteById(id);

        roleService.deleteById(id);

        verify(roleRepository, times(1)).findById(id);
        verify(roleRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeleteById_Role_NotFound() {
        Long id = 1L;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.deleteById(id));

        verify(roleRepository, times(1)).findById(id);
        verify(roleRepository, never()).deleteById(id);
    }



    void testFindById_NotFound() {
        // Arrange
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Role> foundRole = roleService.findById(1L);

        // Assert
        assertFalse(foundRole.isPresent());
    }

    @Test
    void testDeleteById() {
        // Arrange
        when(roleRepository.existsById(1L)).thenReturn(true);

        // Act
        roleService.deleteById(1L);

        // Assert
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdThrowsExceptionWhenRoleNotFound() {
        // Arrange
        when(roleRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RoleNotFoundException.class, () -> roleService.deleteById(1L));
    }
}

