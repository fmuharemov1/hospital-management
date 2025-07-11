package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.dto.PacijentDTO;
import ba.unsa.etf.hospital.dto.PacijentListDTO;
import ba.unsa.etf.hospital.model.Soba;
import ba.unsa.etf.hospital.service.SobaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.SobaNotFoundException;

import java.util.List;
import java.util.Map;
import ba.unsa.etf.hospital.dto.PacijentDTO;

@RestController
@RequestMapping("/sobe")
public class SobaController {
    private final SobaService sobaService;

    public SobaController(SobaService sobaService) {
        this.sobaService = sobaService;
    }

    @GetMapping
    public ResponseEntity<List<Soba>> getAllSobe() {
        List<Soba> sobe = sobaService.getAllSobe();
        return ResponseEntity.ok(sobe);
    }


    @PostMapping
    public Soba createSoba(@Valid @RequestBody Soba soba) {
        return sobaService.saveSoba(soba);
    }

    @GetMapping("/{id}")
    public Soba one(@PathVariable Long id) {
        return sobaService.findById(id)
                .orElseThrow(() -> new SobaNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Soba replaceSoba(@Valid @RequestBody Soba newSoba, @PathVariable Long id) {
        return sobaService.findById(id).map(soba -> {
            soba.setBroj_sobe(newSoba.getBroj_sobe());
            soba.setStatus(newSoba.getStatus());
            soba.setKapacitet(newSoba.getKapacitet());
            return sobaService.saveSoba(soba);
        }).orElseGet(() -> sobaService.saveSoba(newSoba));
    }

    @DeleteMapping("/{id}")
    public void deleteSoba(@PathVariable Long id) {
        sobaService.deleteById(id);
    }

    @PostMapping("/{sobaId}/dodaj-pacijente")
    public ResponseEntity<?> dodajPacijenteUSobu(@PathVariable Long sobaId, @RequestBody PacijentDTO request) {
        System.out.println("Pozvana metoda dodajPacijenteUSobu za sobu " + sobaId);
        sobaService.dodajPacijenteUSobu(sobaId, request.getPacijentIds());
        return ResponseEntity.ok().body(Map.of("message", "Pacijenti uspješno dodani u sobu"));
    }

    @PostMapping("/{sobaId}/otpusti-pacijente")
    public ResponseEntity<?> otpustiPacijenteIzSobe(@PathVariable Long sobaId, @RequestBody PacijentDTO request) {
        sobaService.otpustiPacijenteIzSobe(sobaId, request.getPacijentIds());
        return ResponseEntity.ok().body(Map.of("message", "Pacijenti uspješno otpušteni iz sobe"));
    }
}