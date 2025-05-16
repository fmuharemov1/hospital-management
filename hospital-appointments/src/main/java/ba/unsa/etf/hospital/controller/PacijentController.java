package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.dto.PacijentDTO;
import ba.unsa.etf.hospital.service.PacijentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments/pacijent")
public class PacijentController {

    private final PacijentService pacijentService;

    public PacijentController(PacijentService pacijentService) {
        this.pacijentService = pacijentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacijentDTO> getPacijent(@PathVariable Long id) {
        PacijentDTO pacijent = pacijentService.dohvatiPacijenta(id);
        return ResponseEntity.ok(pacijent);
    }
}
