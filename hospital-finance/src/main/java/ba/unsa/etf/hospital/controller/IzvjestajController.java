package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.service.IzvjestajService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.IzvjestajNotFoundException;
import java.util.List;
@RestController
@RequestMapping("/izvjestaji")
public class IzvjestajController {
    private final IzvjestajService izvjestajService;
    public IzvjestajController(IzvjestajService service) {
        this.izvjestajService = service;
    }

    @GetMapping
    public ResponseEntity<List<Izvjestaj>> getAllIzvjestaji() {
        List<Izvjestaj> izvjestaji = izvjestajService.getAllIzvjestaji();
        return ResponseEntity.ok(izvjestaji);}

    @PostMapping
    public Izvjestaj createIzvjestaj(@RequestBody Izvjestaj izvjestaj) {return izvjestajService.saveIzvjestaj(izvjestaj);}

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
