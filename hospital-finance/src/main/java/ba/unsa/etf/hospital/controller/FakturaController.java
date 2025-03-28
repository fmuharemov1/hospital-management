package ba.unsa.etf.hospital.controller;


import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.service.FakturaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/fakture")
public class FakturaController {
    private final FakturaService fakturaService;
    public FakturaController(FakturaService service) {
        this.fakturaService = service;
    }
    @GetMapping
    public List<Faktura> getAllFakture() { return fakturaService.getAllFakture(); }
    @PostMapping
    public Faktura createFaktura(@RequestBody Faktura faktura) {return fakturaService.saveFaktura(faktura);}
}
