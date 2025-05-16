package ba.unsa.etf.hospital.controller;


import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.service.FakturaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public Faktura createFaktura(@Valid @RequestBody Faktura faktura) {return fakturaService.saveFaktura(faktura);}

    @GetMapping("/{id}")
    public Faktura one(@PathVariable Long id){
        return (Faktura) fakturaService.findById(id).orElseThrow(()->new FakturaNotFoundException(id));
    }
    @PutMapping("/{id}")
    public Faktura replaceFaktura(@Valid @RequestBody Faktura newFaktura, @PathVariable Long id){
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
    public void deleteFaktura(@PathVariable Long id) {
        try {
            fakturaService.deleteById(id);
        } catch (FakturaNotFoundException e) {
            throw new FakturaNotFoundException(id);  // Ovdje se baca specifična greška koja vraća 404
        }
    }
@PostMapping("/batch")
public ResponseEntity<List<Faktura>> saveBatchFakture(@RequestBody List<Faktura> fakture) {
    try {
        List<Faktura> savedFakture = fakturaService.saveBatchFakture(fakture);
        return ResponseEntity.ok(savedFakture);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.emptyList());
    }
}
}
