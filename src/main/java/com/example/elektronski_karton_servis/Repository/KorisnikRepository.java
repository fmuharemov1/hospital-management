package com.example.elektronski_karton_servis.Repository;

import com.example.elektronski_karton_servis.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {
    Optional<Korisnik> findByKorisnikUuid(UUID korisnikUuid);

}




