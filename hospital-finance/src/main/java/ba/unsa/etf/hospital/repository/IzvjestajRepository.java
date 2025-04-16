package ba.unsa.etf.hospital.repository;

import ba.unsa.etf.hospital.model.Izvjestaj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IzvjestajRepository extends JpaRepository<Izvjestaj, Long> {
    Page<Izvjestaj> findAll(Pageable pageable);
    List<Izvjestaj> findByTipIzvjestaja(String tipIzvjestaja);
    @Query("SELECT i FROM Izvjestaj i WHERE i.tipIzvjestaja = :tipIzvjestaja AND i.brojPacijenata > :minBrojPacijenata")
    List<Izvjestaj> findIzvjestajiByTipAndPacijentiGreaterThan(@Param("tipIzvjestaja") String tipIzvjestaja,
                                                               @Param("minBrojPacijenata") int minBrojPacijenata);
    @Query("SELECT i FROM Izvjestaj i WHERE i.tipIzvjestaja = :tipIzvjestaja AND i.finansijskiPregled > :minIznos")
    List<Izvjestaj> findIzvjestajiByTipAndIznosGreaterThan(@Param("tipIzvjestaja") String tipIzvjestaja, @Param("minIznos") double minIznos);
}
