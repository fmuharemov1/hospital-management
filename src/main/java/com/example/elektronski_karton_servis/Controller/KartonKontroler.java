package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Exception.KartonNotFoundException;
import com.example.elektronski_karton_servis.model.Karton;
import com.example.elektronski_karton_servis.Repository.KartonRepository;
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
@RequestMapping("/kartoni")

public class KartonKontroler {

    private final KartonRepository kartonRepository;

    public KartonKontroler(KartonRepository kartonRepository) {
        this.kartonRepository = kartonRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Karton>> all() {
        List<EntityModel<Karton>> kartoni = kartonRepository.findAll().stream()
                .map(karton -> EntityModel.of(karton,
                        linkTo(methodOn(KartonKontroler.class).one(karton.getId())).withSelfRel(),
                        linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni")))
                .collect(Collectors.toList());

        return CollectionModel.of(kartoni, linkTo(methodOn(KartonKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Karton> one(@PathVariable Integer id) {
        Karton karton = kartonRepository.findById(id)
                .orElseThrow(() -> new KartonNotFoundException(id));

        return EntityModel.of(karton,
                linkTo(methodOn(KartonKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni"));
    }


    @PostMapping
    public ResponseEntity<?> newKarton(@Valid @RequestBody Karton noviKartonZahtjev) {
        Karton noviKarton = new Karton();
        noviKarton.setBrojKartona(noviKartonZahtjev.getBrojKartona());
        noviKarton.setDatumKreiranja(noviKartonZahtjev.getDatumKreiranja());
        noviKarton.setPacijentUuid(noviKartonZahtjev.getPacijentUuid());

        Karton sacuvaniKarton = kartonRepository.save(noviKarton);

        EntityModel<Karton> entityModel = EntityModel.of(sacuvaniKarton,
                linkTo(methodOn(KartonKontroler.class).one(sacuvaniKarton.getId())).withSelfRel(),
                linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceKarton(@RequestBody Karton noviKarton, @PathVariable Integer id) {
        Karton azuriraniKarton = kartonRepository.findById(id)
                .map(karton -> {
                    karton.setPacijentUuid(noviKarton.getPacijentUuid());
                    karton.setDatumKreiranja(noviKarton.getDatumKreiranja());
                    karton.setBrojKartona(noviKarton.getBrojKartona());
                    return kartonRepository.save(karton);
                })
                .orElseGet(() -> {
                    noviKarton.setId(id);
                    return kartonRepository.save(noviKarton);
                });

        EntityModel<Karton> entityModel = EntityModel.of(azuriraniKarton,
                linkTo(methodOn(KartonKontroler.class).one(azuriraniKarton.getId())).withSelfRel(),
                linkTo(methodOn(KartonKontroler.class).all()).withRel("kartoni"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKarton(@PathVariable Integer id) {
        if (kartonRepository.existsById(id)) {
            kartonRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new KartonNotFoundException(id);
        }
    }
}


