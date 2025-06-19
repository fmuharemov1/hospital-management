package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import ba.unsa.etf.hospital.service.KorisnikService;
import com.github.fge.jsonpatch.JsonPatch;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.KorisnikNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/korisnici")
public class KorisnikController {
    private final KorisnikService korisnikService;
    @Autowired
    private KorisnikRepository korisnikRepository;

    public KorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping
    public ResponseEntity<List<Korisnik>> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikService.getAllKorisnici();
        return ResponseEntity.ok(korisnici);
    }

    @PostMapping
    public Korisnik createKorisnik(@Valid @RequestBody Korisnik korisnik) {
        return korisnikService.saveKorisnik(korisnik);
    }

    @GetMapping("/{id}")
    public Korisnik one(@PathVariable Long id) {
        return korisnikService.findById(id)
                .orElseThrow(() -> new KorisnikNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Korisnik replaceKorisnik(@Valid @RequestBody Korisnik newKorisnik, @PathVariable Long id) {
        return korisnikService.findById(id).map(korisnik -> {
            korisnik.setIme(newKorisnik.getIme());
            korisnik.setPrezime(newKorisnik.getPrezime());
            korisnik.setEmail(newKorisnik.getEmail());
            korisnik.setLozinka(newKorisnik.getLozinka());
            korisnik.setBr_telefona(newKorisnik.getBr_telefona());
            korisnik.setRole(newKorisnik.getRole());
            return korisnikService.saveKorisnik(korisnik);
        }).orElseGet(() -> korisnikService.saveKorisnik(newKorisnik));
    }

    @DeleteMapping("/{id}")
    public void deleteKorisnik(@PathVariable Long id) {
        korisnikService.deleteById(id);
    }

    @PatchMapping("/{id}/dodijeliSobu")
    public ResponseEntity<?> dodijeliSobuPacijentu(
            @PathVariable Long id,
            @RequestBody JsonPatch patch) {
        return korisnikService.patchKorisnik(id, patch);
    }

    @GetMapping("/tip/pacijenti")
    public List<Korisnik> getPacijenti() {
        return korisnikRepository.findByRole_TipKorisnika("Pacijent");
    }
}