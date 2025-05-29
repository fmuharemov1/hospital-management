package com.example.elektronski_karton_servis.Controller;


import com.example.elektronski_karton_servis.Exception.TerminNotFoundException;
import com.example.elektronski_karton_servis.model.Termin;
import com.example.elektronski_karton_servis.Repository.TerminRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/termini")
public class TerminKontroler {

    private final TerminRepository terminRepository;

    public TerminKontroler(TerminRepository terminRepository) {
        this.terminRepository = terminRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Termin>> all() {
        List<EntityModel<Termin>> termini = terminRepository.findAll().stream()
                .map(termin -> EntityModel.of(termin,
                        linkTo(methodOn(TerminKontroler.class).one(termin.getId())).withSelfRel(),
                        linkTo(methodOn(TerminKontroler.class).all()).withRel("termini")))
                .collect(Collectors.toList());

        return CollectionModel.of(termini, linkTo(methodOn(TerminKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Termin> one(@PathVariable Integer id) {
        Termin termin = terminRepository.findById(id)
                .orElseThrow(() -> new TerminNotFoundException(id));

        return EntityModel.of(termin,
                linkTo(methodOn(TerminKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(TerminKontroler.class).all()).withRel("termini"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Termin>> newTermin(@Valid @RequestBody Termin noviTermin) {
        Termin sacuvaniTermin = terminRepository.save(noviTermin);

        EntityModel<Termin> entityModel = EntityModel.of(sacuvaniTermin,
                linkTo(methodOn(TerminKontroler.class).one(sacuvaniTermin.getId())).withSelfRel(),
                linkTo(methodOn(TerminKontroler.class).all()).withRel("termini"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceTermin(@Valid @RequestBody Termin noviTermin, @PathVariable Integer id) {
        return terminRepository.findById(id)
                .map(termin -> {
                    termin.setPacijentUuid(noviTermin.getPacijentUuid());
                    termin.setOsobljeUuid(noviTermin.getOsobljeUuid());
                    termin.setDatumVrijeme(noviTermin.getDatumVrijeme());
                    return ResponseEntity.ok(terminRepository.save(termin)); // 200 OK - uspješno ažurirano
                })
                .orElseThrow(() -> new TerminNotFoundException(id)); // 404 Not Found ako ne postoji
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTermin(@Valid @PathVariable Integer id) {
        if (terminRepository.existsById(id)) {
            terminRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new TerminNotFoundException(id);
        }
    }

}
