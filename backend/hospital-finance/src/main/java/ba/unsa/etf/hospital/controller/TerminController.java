package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.TerminNotFoundException;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.RemoteTerminService;
import ba.unsa.etf.hospital.service.TerminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/termini")
public class TerminController {
    private final TerminService terminService;
    private final RemoteTerminService remoteTerminService;

    @Autowired
    public TerminController(TerminService terminService, RemoteTerminService remoteTerminService) {
        this.terminService = terminService;
        this.remoteTerminService = remoteTerminService;
    }
    @PostMapping
    public Termin createTermin(@RequestBody Termin termin) {
        return terminService.saveTermin(termin);
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
    @PostMapping("/novi")
    public ResponseEntity<Termin> createTerminWithFaktura(@RequestBody Termin termin) {
        Termin saved = terminService.createTerminWithNewFaktura(termin);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public void deleteTermin(@PathVariable Long id){
        terminService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Termin one(@PathVariable Long id){
        return terminService.findById(id)
                .orElseThrow(()->new TerminNotFoundException(id));
    }
    @GetMapping
    public ResponseEntity<Page<Termin>> getAllTermini(
            @RequestParam(defaultValue = "0") int page,  // Stranica (default je 0)
            @RequestParam(defaultValue = "10") int size, // Broj elemenata po stranici (default je 10)
            @RequestParam(defaultValue = "asc") String sort) { // Sortiranje, može biti 'asc' ili 'desc'

        // Pozivanje servisne metode za dobijanje paginiranih i sortiranih rezultata
        Page<Termin> terminiPage;

        if ("desc".equalsIgnoreCase(sort)) {
            terminiPage = terminService.getSortedAndPaginatedTerminiDesc(page, size);  // Paginacija i sortiranje opadajuće
        } else {
            terminiPage = terminService.getSortedAndPaginatedTermini(page, size);  // Paginacija i sortiranje rastuće
        }

        return ResponseEntity.ok(terminiPage);
    }
}
