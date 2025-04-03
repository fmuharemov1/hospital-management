package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.TerminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/termini")
public class TerminController {
    private final TerminService terminService;
    public TerminController(TerminService service) {this.terminService = service;}
    @GetMapping
    public ResponseEntity<List<Termin>> getAllTermini() {
        List<Termin> termini = terminService.getAllTermini();
        return ResponseEntity.ok(termini);}
    @PostMapping
    public Termin createTermin(@RequestBody Termin termin) {
        return terminService.saveTermin(termin);
    }

    @GetMapping("/{id}")
    public Termin one(@PathVariable Long id){
        return terminService.findById(id)
                .orElseThrow(()->new TerminNotFoundException(id));
    }
    @PutMapping("/{id}")
    public Termin replaceTermin(@RequestBody Termin newTermin, @PathVariable Long id){
        return terminService.findById(id)
                .map(termin -> {
                    termin.setTerminUuid(newTermin.getTerminUuid());
                    termin.setPacijent(newTermin.getPacijent());
                    termin.setOsoblje(newTermin.getOsoblje());
                    termin.setFaktura(newTermin.getFaktura());
                    termin.setDatumVrijeme(newTermin.getDatumVrijeme());
                    return terminService.saveTermin(termin);
                })
                .orElseGet(()->{
                    return terminService.saveTermin(newTermin);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteTermin(@PathVariable Long id){
        terminService.deleteById(id);
    }
}
