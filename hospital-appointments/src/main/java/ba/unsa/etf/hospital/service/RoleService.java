package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.RoleNotFoundException;
import ba.unsa.etf.hospital.model.Role;
import ba.unsa.etf.hospital.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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

    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public void deleteById(Long id) {
<<<<<<< HEAD:hospital-finance/src/main/java/ba/unsa/etf/hospital/service/RoleService.java
        // Provjeri da li role postoji
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));  // Ako role nije pronađena, baci izuzetak

        roleRepository.deleteById(id);  // Ako je pronađena, obriši
=======
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException(id);
        }
        roleRepository.deleteById(id);
>>>>>>> appointments-branch:hospital-appointments/src/main/java/ba/unsa/etf/hospital/service/RoleService.java
    }
}
