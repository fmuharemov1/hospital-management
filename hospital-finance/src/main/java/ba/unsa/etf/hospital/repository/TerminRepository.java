package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Termin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TerminRepository extends JpaRepository<Termin, Long> {
    Page<Termin> findAllByOrderByDatumVrijemeAsc(Pageable pageable);  // Sortira po datumu i vremenu u rastućem redosledu
    Page<Termin> findAllByOrderByDatumVrijemeDesc(Pageable pageable);
}
