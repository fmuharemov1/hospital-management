package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    long countByRole_Soba_Id(Long sobaId);
    List<Korisnik> findAllById(Iterable<Long> ids);
    List<Korisnik> findByRole_TipKorisnika(String tipKorisnika);
}
