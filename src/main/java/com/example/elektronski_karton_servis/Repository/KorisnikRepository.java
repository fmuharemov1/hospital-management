package com.example.elektronski_karton_servis.Repository;

import com.example.elektronski_karton_servis.model.Korisnik;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {
    Optional<Korisnik> findByKorisnikUuid(UUID korisnikUuid);
    // Custom upiti (nisu automatski generirani)
    List<Korisnik> findByImeIgnoreCaseAndPrezimeIgnoreCase(String ime, String prezime);

    @Query("SELECT k FROM Korisnik k WHERE LOWER(k.ime) LIKE %:tekst% OR LOWER(k.prezime) LIKE %:tekst%")
    List<Korisnik> findByImeIliPrezimeSadrziIgnoreCase(@Param("tekst") String tekst);

    List<Korisnik> findByRoleId(Integer roleId);

    // Paginacija i sortiranje (automatski podržano od strane JpaRepository)
    Page<Korisnik> findAll(Pageable pageable);
}




