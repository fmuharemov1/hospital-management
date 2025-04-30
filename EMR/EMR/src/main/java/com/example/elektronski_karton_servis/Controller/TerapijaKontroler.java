package com.example.elektronski_karton_servis.Controller;


import com.example.elektronski_karton_servis.Exception.TerapijaNotFoundException;
import com.example.elektronski_karton_servis.model.Terapija;
import com.example.elektronski_karton_servis.Repository.TerapijaRepository;
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
@RequestMapping("/terapije")

public class TerapijaKontroler {

    private final TerapijaRepository terapijaRepository;

    public TerapijaKontroler(TerapijaRepository terapijaRepository) {
        this.terapijaRepository = terapijaRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Terapija>> all() {
        List<EntityModel<Terapija>> terapije = terapijaRepository.findAll().stream()
                .map(terapija -> EntityModel.of(terapija,
                        linkTo(methodOn(TerapijaKontroler.class).one(terapija.getId())).withSelfRel(),
                        linkTo(methodOn(TerapijaKontroler.class).all()).withRel("terapije")))
                .collect(Collectors.toList());

        return CollectionModel.of(terapije, linkTo(methodOn(TerapijaKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Terapija> one(@PathVariable Integer id) {
        Terapija terapija = terapijaRepository.findById(id)
                .orElseThrow(() -> new TerapijaNotFoundException(id));

        return EntityModel.of(terapija,
                linkTo(methodOn(TerapijaKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(TerapijaKontroler.class).all()).withRel("terapije"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Terapija>> newTerapija(@Valid @RequestBody Terapija novaTerapija) {
        Terapija sacuvanaTerapija = terapijaRepository.save(novaTerapija);

        EntityModel<Terapija> entityModel = EntityModel.of(sacuvanaTerapija,
                linkTo(methodOn(TerapijaKontroler.class).one(sacuvanaTerapija.getId())).withSelfRel(),
                linkTo(methodOn(TerapijaKontroler.class).all()).withRel("terapije"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceTerapija(@Valid @RequestBody Terapija novaTerapija, @PathVariable Integer id) {
        return terapijaRepository.findById(id)
                .map(terapija -> {
                    terapija.setOsobljeUuid(novaTerapija.getOsobljeUuid());
                    terapija.setNaziv(novaTerapija.getNaziv());
                    terapija.setOpis(novaTerapija.getOpis());
                    terapija.setDatumPocetka(novaTerapija.getDatumPocetka());
                    terapija.setDatumZavrsetka(novaTerapija.getDatumZavrsetka());
                    return ResponseEntity.ok(terapijaRepository.save(terapija)); // 200 OK - uspješno ažurirano
                })
                .orElseThrow(() -> new TerapijaNotFoundException(id)); // 404 Not Found ako ne postoji
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTerapija(@Valid @PathVariable Integer id) {
        if (terapijaRepository.existsById(id)) {
            terapijaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new TerapijaNotFoundException(id);
        }
    }
}