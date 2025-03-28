package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.TerminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/termini")
public class TerminController {
    private final TerminService terminService;
    public TerminController(TerminService service) {this.terminService = service;}
    @GetMapping
    public List<Termin> getAllTermini() {return terminService.getAllTermini();}
    @PostMapping
    public Termin createTermin(@RequestBody Termin termin) {
        return terminService.saveTermin(termin);
    }

}
