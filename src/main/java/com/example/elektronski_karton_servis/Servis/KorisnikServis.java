package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.model.Korisnik;
import com.example.elektronski_karton_servis.Repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KorisnikServis {

    @Autowired
    private KorisnikRepository korisnikRepository;

    public List<Korisnik> getAllKorisnici() {
        return korisnikRepository.findAll();
    }

    public Optional<Korisnik> findById(Integer id) {
        return korisnikRepository.findById(id);
    }

    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }

    public void deleteById(Integer id) {
        korisnikRepository.deleteById(id);
    }
}





