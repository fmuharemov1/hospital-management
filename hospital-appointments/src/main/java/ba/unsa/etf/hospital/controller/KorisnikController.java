package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.service.KorisnikService;
import jakarta.servlet.ServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/korisnici")
public class KorisnikController {
    private final KorisnikService korisnikService;
    public KorisnikController(KorisnikService service) {
        this.korisnikService = service;
    }
    @GetMapping
    public ResponseEntity<List<Korisnik>> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikService.getAllKorisnici();
        return ResponseEntity.ok(korisnici);}
    @PostMapping
    public Korisnik createKorisnik(@RequestBody Korisnik korisnik) {
        return korisnikService.saveKorisnik(korisnik);
    }

    @GetMapping("/{id}")
    public Korisnik one(@PathVariable Long id){
        return korisnikService.findById(id)
                .orElseThrow(()-> new KorisnikNotFoundException(id));
    }
    @PutMapping("/{id}")
    public Korisnik replaceKorisnik(@RequestBody Korisnik newKorisnik, @PathVariable Long id, ServletRequest servletRequest){
        return korisnikService.findById(id)
                .map(korisnik -> {
                    korisnik.setRole(newKorisnik.getRole());
                    korisnik.setKorisnikUuid(newKorisnik.getKorisnikUuid());
                    return korisnikService.saveKorisnik(korisnik);
                })
                .orElseGet(() -> {
                    return korisnikService.saveKorisnik(newKorisnik);
                });
    }
    @DeleteMapping("/{id}")
    public void deleteKorisnik(@PathVariable Long id){
        korisnikService.deleteById(id);
    }

}
