package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.repository.RoleRepository;

import java.util.List;

public class RoleService {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
