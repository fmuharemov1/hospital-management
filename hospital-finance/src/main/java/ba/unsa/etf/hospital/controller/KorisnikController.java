package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.service.KorisnikService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/korisnici")
public class KorisnikController {
    private final KorisnikService korisnikService;
    public KorisnikController(KorisnikService service) {
        this.korisnikService = service;
    }
    @GetMapping
    public List<Korisnik> getAllKorisnici() {
        return korisnikService.getAllKorisnici();
    }
    @PostMapping
    public Korisnik createKorisnik(@RequestBody Korisnik korisnik) {
        return korisnikService.saveKorisnik(korisnik);
    }

}
