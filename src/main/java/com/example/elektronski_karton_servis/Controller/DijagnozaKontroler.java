package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Exception.DijagnozaNotFoundException;
import com.example.elektronski_karton_servis.Exception.KartonNotFoundException;
import com.example.elektronski_karton_servis.model.Dijagnoza;
import com.example.elektronski_karton_servis.Repository.DijagnozaRepository; // Pretpostavljam da je ovo ispravan put
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
@RequestMapping("/dijagnoze")

public class DijagnozaKontroler {

    private final DijagnozaRepository dijagnozaRepository;

    public DijagnozaKontroler(DijagnozaRepository dijagnozaRepository) {
        this.dijagnozaRepository = dijagnozaRepository;
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




