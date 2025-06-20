package com.example.client_service.controller;

import com.example.client_service.dto.IzvjestajDto;
import com.example.client_service.service.IzvjestajService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/izvjestaji")
@CrossOrigin(origins = "http://localhost:3000")
public class IzvjestajController {

    private final IzvjestajService izvjestajService;

    public IzvjestajController(IzvjestajService izvjestajService) {
        this.izvjestajService = izvjestajService;
    }

    @GetMapping
    public ResponseEntity<Page<IzvjestajDto>> getAllIzvjestaji(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<IzvjestajDto> izvjestaji = izvjestajService.getAllIzvjestaji(page, size);
        return ResponseEntity.ok(izvjestaji);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<IzvjestajDto>> filterIzvjestaji(
            @RequestParam String tipIzvjestaja,
            @RequestParam(required = false) Integer minBrojPacijenata,
            @RequestParam(required = false) Double minIznos) {

        List<IzvjestajDto> izvjestaji = izvjestajService.filterIzvjestaji(
                tipIzvjestaja, minBrojPacijenata, minIznos);
        return ResponseEntity.ok(izvjestaji);
    }

    @PostMapping
    public ResponseEntity<IzvjestajDto> createIzvjestaj(@RequestBody IzvjestajDto izvjestaj) {
        IzvjestajDto created = izvjestajService.createIzvjestaj(izvjestaj);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IzvjestajDto> getIzvjestaj(@PathVariable Long id) {
        IzvjestajDto izvjestaj = izvjestajService.getIzvjestajById(id);
        return ResponseEntity.ok(izvjestaj);
    }
}