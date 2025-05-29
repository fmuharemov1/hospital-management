package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.exception.IzvjestajNotFoundException;
import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.service.IzvjestajService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/izvjestaji")
public class IzvjestajController {
    private final IzvjestajService izvjestajService;
    public IzvjestajController(IzvjestajService service) {
        this.izvjestajService = service;
    }

    @GetMapping
    public ResponseEntity<Page<Izvjestaj>> getAllIzvjestaji(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Izvjestaj> izvjestaji = izvjestajService.getIzvjestajiSortedByFinansijskiPregled(page, size);
        return ResponseEntity.ok(izvjestaji);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Izvjestaj>> filterIzvjestaji(
            @RequestParam String tipIzvjestaja,
            @RequestParam(required = false) Integer minBrojPacijenata,
            @RequestParam(required = false) Double minIznos) {

        List<Izvjestaj> izvjestaji;

        if (minBrojPacijenata != null) {
            izvjestaji = izvjestajService.getIzvjestajiByTipAndMinPacijenti(tipIzvjestaja, minBrojPacijenata);
        } else if (minIznos != null) {
            izvjestaji = izvjestajService.getIzvjestajiByTipAndMinIznos(tipIzvjestaja, minIznos);
        } else {
            izvjestaji = izvjestajService.getIzvjestajiByTip(tipIzvjestaja); // samo po tipu
        }

        return ResponseEntity.ok(izvjestaji);
    }
    @PostMapping
    public Izvjestaj createIzvjestaj(@RequestBody Izvjestaj izvjestaj) {return izvjestajService.saveIzvjestaj(izvjestaj);}

    @PostMapping("/batch")
    public ResponseEntity<List<Izvjestaj>> saveBatchIzvjestaji(@RequestBody List<Izvjestaj> izvjestaji) {
        try {
            // Pozivanje servisne metode za batch unos
            List<Izvjestaj> savedIzvjestaji = izvjestajService.saveBatchIzvjestaji(izvjestaji);
            return ResponseEntity.ok(savedIzvjestaji);  // Vraća listu uspješno sačuvanih izvještaja
        } catch (Exception e) {
            // Ako dođe do greške, vraćamo 500 grešku i praznu listu
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());  // Vraća praznu listu ako dođe do greške
        }
    }
    @GetMapping("/{id}")
    public Izvjestaj one(@PathVariable Long id){
        return izvjestajService.findById(id)
                .orElseThrow(() ->new IzvjestajNotFoundException(id));
    }
    @PutMapping("/{id}")
    public Izvjestaj replaceIzvjestaj(@RequestBody Izvjestaj newIzvjestaj, @PathVariable Long id){
        return izvjestajService.findById(id)
                .map(izvjestaj -> {
                    izvjestaj.setOsoblje(newIzvjestaj.getOsoblje());
                    izvjestaj.setTipIzvjestaja(newIzvjestaj.getTipIzvjestaja());
                    izvjestaj.setBrojPacijenata(newIzvjestaj.getBrojPacijenata());
                    izvjestaj.setBrojTermina(newIzvjestaj.getBrojTermina());
                    izvjestaj.setFinancijskiPregled(newIzvjestaj.getFinansijskiPregled());
                    izvjestaj.setDatumGenerisanja(newIzvjestaj.getDatumGenerisanja());
                    return izvjestajService.saveIzvjestaj(izvjestaj);
                })
                .orElseGet(() -> {
                    return izvjestajService.saveIzvjestaj(newIzvjestaj);
                });
    }
    @DeleteMapping("/{id}")
    public void deleteIzvjestaj(@PathVariable Long id){
        izvjestajService.deleteById(id);
    }
}
