package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Obavijest;
import ba.unsa.etf.hospital.service.ObavijestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.ObavijestNotFoundException;

import java.util.List;

@RestController
@RequestMapping("/obavijesti")
public class ObavijestController {
    private final ObavijestService obavijestService;

    public ObavijestController(ObavijestService obavijestService) {
        this.obavijestService = obavijestService;
    }

    @GetMapping
    public ResponseEntity<List<Obavijest>> getAllObavijesti() {
        List<Obavijest> obavijesti = obavijestService.getAllObavijesti();
        return ResponseEntity.ok(obavijesti);
    }

    @PostMapping
    public Obavijest createObavijest(@Valid @RequestBody Obavijest obavijest) {
        return obavijestService.saveObavijest(obavijest);
    }

    @GetMapping("/{id}")
    public Obavijest one(@PathVariable Long id) {
        return obavijestService.findById(id)
                .orElseThrow(() -> new ObavijestNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Obavijest replaceObavijest(@Valid @RequestBody Obavijest newObavijest, @PathVariable Long id) {
        return obavijestService.findById(id).map(obavijest -> {
            obavijest.setSadrzaj(newObavijest.getSadrzaj());
            obavijest.setDatum_vrijeme(newObavijest.getDatum_vrijeme());
            return obavijestService.saveObavijest(obavijest);
        }).orElseGet(() -> obavijestService.saveObavijest(newObavijest));
    }

    @DeleteMapping("/{id}")
    public void deleteObavijest(@PathVariable Long id) {
        obavijestService.deleteById(id);
    }
}
