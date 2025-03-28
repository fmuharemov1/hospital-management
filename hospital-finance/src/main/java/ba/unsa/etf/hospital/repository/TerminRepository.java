package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminRepository extends JpaRepository<Termin, Long> {
}
