package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
