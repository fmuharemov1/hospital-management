package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Exception.DijagnozaNotFoundException;
import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.example.elektronski_karton_servis.Repository.DijagnozaRepository; // Vjerovatno ćete ovo ukloniti
import com.example.elektronski_karton_servis.Servis.DijagnozaServis; // Dodajte ovaj import
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/dijagnoze")

public class DijagnozaKontroler {

    private final DijagnozaRepository dijagnozaRepository;
    private final DijagnozaServis dijagnozaServis;

    public DijagnozaKontroler(DijagnozaRepository dijagnozaRepository, DijagnozaServis dijagnozaServis) {
        this.dijagnozaRepository = dijagnozaRepository;
        this.dijagnozaServis = dijagnozaServis;
    }

    @GetMapping
    public CollectionModel<EntityModel<Dijagnoza>> all() {
        List<EntityModel<Dijagnoza>> dijagnoze = dijagnozaRepository.findAll().stream()
                .map(dijagnoza -> EntityModel.of(dijagnoza,
                        linkTo(methodOn(DijagnozaKontroler.class).one(dijagnoza.getId())).withSelfRel(),
                        linkTo(methodOn(DijagnozaKontroler.class).all()).withRel("dijagnoze")))
                .collect(Collectors.toList());

        return CollectionModel.of(dijagnoze, linkTo(methodOn(DijagnozaKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Dijagnoza> one(@PathVariable Integer id) {
        Dijagnoza dijagnoza = dijagnozaRepository.findById(id)
                .orElseThrow(() -> new DijagnozaNotFoundException(id));

        return EntityModel.of(dijagnoza,
                linkTo(methodOn(DijagnozaKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(DijagnozaKontroler.class).all()).withRel("dijagnoze"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Dijagnoza>> newDijagnoza(@Valid @RequestBody Dijagnoza novaDijagnoza) {
        Dijagnoza sacuvanaDijagnoza = dijagnozaRepository.save(novaDijagnoza);

        EntityModel<Dijagnoza> entityModel = EntityModel.of(sacuvanaDijagnoza,
                linkTo(methodOn(DijagnozaKontroler.class).one(sacuvanaDijagnoza.getId())).withSelfRel(),
                linkTo(methodOn(DijagnozaKontroler.class).all()).withRel("dijagnoze"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


   @PutMapping("/{id}")
   public ResponseEntity<?> replaceDijagnoza(@RequestBody Dijagnoza novaDijagnoza, @PathVariable Integer id) {
       return dijagnozaRepository.findById(id)
               .map(dijagnoza -> {
                   dijagnoza.setKartonId(novaDijagnoza.getKartonId());
                   dijagnoza.setOsobljeUuid(novaDijagnoza.getOsobljeUuid());
                   dijagnoza.setNaziv(novaDijagnoza.getNaziv());
                   dijagnoza.setOpis(novaDijagnoza.getOpis());
                   dijagnoza.setDatumDijagnoze(novaDijagnoza.getDatumDijagnoze());
                   return ResponseEntity.ok(dijagnozaRepository.save(dijagnoza)); // 200 OK - uspješno ažurirano
               })
               .orElseThrow(() -> new DijagnozaNotFoundException(id)); // 404 Not Found ako ne postoji
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDijagnoza(@PathVariable Integer id) {
        if (dijagnozaRepository.existsById(id)) {
            dijagnozaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new DijagnozaNotFoundException(id);
        }
    }

}




