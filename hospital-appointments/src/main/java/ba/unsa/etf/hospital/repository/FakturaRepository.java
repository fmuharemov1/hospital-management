package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Faktura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FakturaRepository extends JpaRepository<Faktura, Long> {
}
