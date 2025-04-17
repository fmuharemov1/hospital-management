package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.TerminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.TerminNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/termini")
public class TerminController {
    private final TerminService terminService;

    public TerminController(TerminService terminService) {
        this.terminService = terminService;
    }

    @GetMapping
    public ResponseEntity<List<Termin>> getAllTermini() {
        List<Termin> termini = terminService.getAllTermini();
        return ResponseEntity.ok(termini);
    }

    @PostMapping
    public Termin createTermin(@Valid @RequestBody Termin termin) {
        return terminService.saveTermin(termin);
    }

    @GetMapping("/{id}")
    public Termin one(@PathVariable Long id) {
        return terminService.findById(id)
                .orElseThrow(() -> new TerminNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Termin replaceTermin(@Valid @RequestBody Termin newTermin, @PathVariable Long id) {
        return terminService.findById(id).map(termin -> {
            termin.setPacijent(newTermin.getPacijent());
            termin.setOsoblje(newTermin.getOsoblje());
            termin.setObavijest(newTermin.getObavijest());
            termin.setStatus(newTermin.getStatus());
            termin.setDatumVrijeme(newTermin.getDatumVrijeme());
            termin.setTrajanje(newTermin.getTrajanje());
            termin.setMeet_link(newTermin.getMeet_link());
            return terminService.saveTermin(termin);
        }).orElseGet(() -> terminService.saveTermin(newTermin));
    }

    @DeleteMapping("/{id}")
    public void deleteTermin(@PathVariable Long id) {
        terminService.deleteById(id);
    }

    @PostMapping("/createWithNotification")
    public Termin createTerminWithNotification(@Valid @RequestBody Termin termin) {
        return terminService.createTerminWithNotification(termin);
    }
}