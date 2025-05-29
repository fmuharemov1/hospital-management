package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Exception.KartonNotFoundException;
import com.example.elektronski_karton_servis.Servis.KartonServis; // Importujte servis
import com.example.elektronski_karton_servis.model.Karton;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/kartoni")
public class KartonKontroler {

    private final KartonServis kartonServis; // Koristite servis

    public KartonKontroler(KartonServis kartonServis) { // Injektujte servis u konstruktoru
        this.kartonServis = kartonServis;
    }

    @GetMapping
    public CollectionModel<EntityModel<Karton>> all() {
        List<EntityModel<Karton>> kartoni = kartonServis.getAllKartoni().stream() // Koristite servis
                .map(karton -> EntityModel.of(karton,
                        linkTo(methodOn(KartonKontroler.class).one(karton.getId())).withSelfRel(),
                        linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni")))
                .collect(Collectors.toList());

        return CollectionModel.of(kartoni, linkTo(methodOn(KartonKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Karton>> one(@PathVariable Integer id) {
        Optional<Karton> karton = kartonServis.findById(id);
        return karton.map(k -> ResponseEntity.ok(EntityModel.of(k,
                        linkTo(methodOn(KartonKontroler.class).one(id)).withSelfRel(),
                        linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni"))))
                .orElse(ResponseEntity.notFound().build()); // ISPRAVNO
    }

    @PostMapping
    public ResponseEntity<?> newKarton(@Valid @RequestBody Karton noviKartonZahtjev) {
        Karton sacuvaniKarton = kartonServis.saveKarton(noviKartonZahtjev); // Koristite servis

        EntityModel<Karton> entityModel = EntityModel.of(sacuvaniKarton,
                linkTo(methodOn(KartonKontroler.class).one(sacuvaniKarton.getId())).withSelfRel(),
                linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceKarton(@RequestBody Karton noviKarton, @PathVariable Integer id) {
        Optional<Karton> azuriraniKarton = kartonServis.updateKarton(id, noviKarton);
        return azuriraniKarton.map(karton -> {
                    EntityModel<Karton> entityModel = EntityModel.of(karton,
                            linkTo(methodOn(KartonKontroler.class).one(karton.getId())).withSelfRel(),
                            linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni"));
                    return ResponseEntity.ok(entityModel); // Promijenjeno na ResponseEntity.ok()
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKarton(@PathVariable Integer id) {
        if (kartonServis.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build(); // ISPRAVNO
        }
    }
}