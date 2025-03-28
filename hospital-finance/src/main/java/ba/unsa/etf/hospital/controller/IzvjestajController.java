package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Izvjestaj;
import ba.unsa.etf.hospital.service.IzvjestajService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/izvjestaji")
public class IzvjestajController {
    private final IzvjestajService izvjestajService;
    public IzvjestajController(IzvjestajService service) {
        this.izvjestajService = service;
    }
    @GetMapping
    public List<Izvjestaj> getAllIzvjestaji() {return izvjestajService.getAllIzvjestaji();}
    @PostMapping
    public Izvjestaj createIzvjestaj(@RequestBody Izvjestaj izvjestaj) {return izvjestajService.saveIzvjestaj(izvjestaj);}
}
