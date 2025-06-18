package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Faktura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FakturaRepository extends JpaRepository<Faktura, Long> {
    List<Faktura> findByKorisnikId(Long korisnikId);
    List<Faktura> findByTerminId(Long terminId);

}
