package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Repository.RoleRepository;
import com.example.elektronski_karton_servis.Servis.RoleServis;
import com.example.elektronski_karton_servis.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServisTest {

    private RoleRepository repo;
    private RoleServis servis;

    @BeforeEach
    void init() {
        repo = mock(RoleRepository.class);
        servis = new RoleServis();
        servis = spy(servis);
        servis.roleRepository = repo;
    }

    private Role sample() {
        Role r = new Role();
        r.setId(1);
        r.setTipKorisnika("Medicinska sestra");
        r.setSmjena("Noćna");
        r.setOdjeljenje("Hirurgija");
        return r;
    }

    @Test
    void getAllRoles() {
        when(repo.findAll()).thenReturn(List.of(sample()));
        assertEquals(1, servis.getAllRoles().size());
    }

    @Test
    void findById_found() {
        when(repo.findById(1)).thenReturn(Optional.of(sample()));
        assertTrue(servis.findById(1).isPresent());
    }

    @Test
    void findById_notFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertTrue(servis.findById(1).isEmpty());
    }

    @Test
    void save() {
        Role r = sample();
        when(repo.save(r)).thenReturn(r);
        assertEquals(r, servis.saveRole(r));
    }

    @Test
    void delete() {
        servis.deleteById(1);
        verify(repo, times(1)).deleteById(1);
    }
}