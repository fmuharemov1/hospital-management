package ba.unsa.etf.hospital.controller;


import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.service.FakturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import java.util.List;
@RestController
@RequestMapping("/fakture")
public class FakturaController {
    private final FakturaService fakturaService;
    public FakturaController(FakturaService service) {
        this.fakturaService = service;
    }
    @GetMapping
    public ResponseEntity<List<Faktura>> getAllFakture() {
        List<Faktura> fakture = fakturaService.getAllFakture();
        return ResponseEntity.ok(fakture);}
    @PostMapping
    public Faktura createFaktura(@RequestBody Faktura faktura) {return fakturaService.saveFaktura(faktura);}

    @GetMapping("/{id}")
    public Faktura one(@PathVariable Long id){
        return (Faktura) fakturaService.findById(id).orElseThrow(()->new FakturaNotFoundException(id));
    }
    @PutMapping("/{id}")
    public Faktura replaceFaktura(@RequestBody Faktura newFaktura, @PathVariable Long id){
        return fakturaService.findById(id).map(faktura ->{
            faktura.setIznos(newFaktura.getIznos());
            faktura.setStatus(newFaktura.getStatus());
            faktura.setMetod(newFaktura.getMetod());
            return fakturaService.saveFaktura(faktura);
        })
                .orElseGet(()-> {
                    return fakturaService.saveFaktura(newFaktura);
                });
    }
    @DeleteMapping("/{id}")
    public void deleteFaktura(@PathVariable Long id){
        fakturaService.deleteById(id);
    }
}
