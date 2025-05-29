package ba.unsa.etf.hospital.controller;

import ba.unsa.etf.hospital.dto.TerminDTO;
import ba.unsa.etf.hospital.model.Termin;
import ba.unsa.etf.hospital.service.TerminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ba.unsa.etf.hospital.exception.TerminNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/termini")

public class TerminController {
    private final TerminService terminService;

    public TerminController(TerminService terminService) {
        this.terminService = terminService;
    }

    // DTO mapper
    private TerminDTO mapToDTO(Termin termin) {
        return new TerminDTO(
                termin.getId(),
                termin.getDatum(),                     // getter mora postojati u Termin modelu
                termin.getVrijemePocetka(),            // getter mora postojati u Termin modelu
                termin.getVrijemeKraja(),              // getter mora postojati u Termin modelu
                termin.getPacijent() != null ? termin.getPacijent().getId() : null,
                termin.getOsoblje() != null ? termin.getOsoblje().getId() : null
        );
    }

    // GET svi termini (kao DTO)
    @GetMapping
    public List<TerminDTO> getAllTermini() {
        List<Termin> termini = terminService.getAllTermini();
        return termini.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // POST novi termin
    @PostMapping
    public Termin createTermin(@Valid @RequestBody Termin termin) {
        return terminService.saveTermin(termin);
    }

    // GET jedan termin
    @GetMapping("/{id}")
    public Termin one(@PathVariable Long id) {
        return terminService.findById(id)
                .orElseThrow(() -> new TerminNotFoundException(id));
    }

    // PUT update
    @PutMapping("/{id}")
    public Termin replaceTermin(@Valid @RequestBody Termin newTermin, @PathVariable Long id) {
        return terminService.findById(id).map(termin -> {
            termin.setPacijent(newTermin.getPacijent());
            termin.setOsoblje(newTermin.getOsoblje());
            termin.setObavijest(newTermin.getObavijest());
            termin.setStatus(newTermin.getStatus());
            termin.setDatumVrijeme(newTermin.getDatumVrijeme());
            termin.setTrajanje(newTermin.getTrajanje());
            termin.setMeet_link(newTermin.getMeet_link());
            return terminService.saveTermin(termin);
        }).orElseGet(() -> terminService.saveTermin(newTermin));
    }

    // DELETE termin
    @DeleteMapping("/{id}")
    public void deleteTermin(@PathVariable Long id) {
        terminService.deleteById(id);
    }

    // POST sa notifikacijom
    @PostMapping("/kreirajSaNotifikacijom")
    public Termin kreirajTerminSaNotifikacijom(@Valid @RequestBody Termin termin) {
        return terminService.kreirajTerminSaNotifikacijom(termin);
    }
}
