package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Role;
import com.example.elektronski_karton_servis.Repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServisTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServis roleServis;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllRoles() {
        Role role1 = new Role();
        role1.setId(1); // Pretpostavljam da je ID Integer
        role1.setTipKorisnika("Pacijent");
        role1.setSmjena("Jutarnja");
        role1.setOdjeljenje("Opšta praksa");

        Role role2 = new Role();
        role2.setId(2); // Pretpostavljam da je ID Integer
        role2.setTipKorisnika("Doktor");
        role2.setSmjena("Popodnevna");
        role2.setOdjeljenje("Kardiologija");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        // Testiranje metode
        var roles = roleServis.getAllRoles();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals(role1.getId(), roles.get(0).getId());
        assertEquals(role1.getTipKorisnika(), roles.get(0).getTipKorisnika());
        assertEquals(role1.getSmjena(), roles.get(0).getSmjena());
        assertEquals(role1.getOdjeljenje(), roles.get(0).getOdjeljenje());
        assertEquals(role2.getId(), roles.get(1).getId());
        assertEquals(role2.getTipKorisnika(), roles.get(1).getTipKorisnika());
        assertEquals(role2.getSmjena(), roles.get(1).getSmjena());
        assertEquals(role2.getOdjeljenje(), roles.get(1).getOdjeljenje());

        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        Role role = new Role();
        role.setId(1); // Pretpostavljam da je ID Integer
        role.setTipKorisnika("Sestra");
        role.setSmjena("Noćna");
        role.setOdjeljenje("Hirurgija");

        when(roleRepository.findById(1)).thenReturn(Optional.of(role));

        // Testiranje metode
        Optional<Role> foundRole = roleServis.findById(1);

        assertTrue(foundRole.isPresent());
        assertEquals(role.getId(), foundRole.get().getId());
        assertEquals(role.getTipKorisnika(), foundRole.get().getTipKorisnika());
        assertEquals(role.getSmjena(), foundRole.get().getSmjena());
        assertEquals(role.getOdjeljenje(), foundRole.get().getOdjeljenje());

        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(roleRepository.findById(1)).thenReturn(Optional.empty());

        // Testiranje metode
        Optional<Role> foundRole = roleServis.findById(1);

        assertFalse(foundRole.isPresent());

        verify(roleRepository, times(1)).findById(1);
    }

    @Test
    public void testSaveRole() {
        Role roleToSave = new Role();
        roleToSave.setTipKorisnika("Farmaceut");
        roleToSave.setSmjena("Puna");
        roleToSave.setOdjeljenje("Apoteka");

        Role savedRole = new Role();
        savedRole.setId(1); // Pretpostavljam da se ID generiše prilikom čuvanja
        savedRole.setTipKorisnika(roleToSave.getTipKorisnika());
        savedRole.setSmjena(roleToSave.getSmjena());
        savedRole.setOdjeljenje(roleToSave.getOdjeljenje());

        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        // Testiranje metode
        Role result = roleServis.saveRole(roleToSave);

        assertNotNull(result);
        assertEquals(savedRole.getId(), result.getId());
        assertEquals(roleToSave.getTipKorisnika(), result.getTipKorisnika());
        assertEquals(roleToSave.getSmjena(), result.getSmjena());
        assertEquals(roleToSave.getOdjeljenje(), result.getOdjeljenje());

        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void testDeleteById() {
        int roleIdToDelete = 1;
        doNothing().when(roleRepository).deleteById(roleIdToDelete);

        // Testiranje metode
        roleServis.deleteById(roleIdToDelete);

        verify(roleRepository, times(1)).deleteById(roleIdToDelete);
    }
}


