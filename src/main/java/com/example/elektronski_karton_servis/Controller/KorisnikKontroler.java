package com.example.elektronski_karton_servis.Controller;


import com.example.elektronski_karton_servis.Exception.KorisnikNotFoundException;
import com.example.elektronski_karton_servis.model.Korisnik;
import com.example.elektronski_karton_servis.Repository.KorisnikRepository;
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
@RequestMapping("/korisnici")

public class KorisnikKontroler {

    private final KorisnikRepository korisnikRepository;

    public KorisnikKontroler(KorisnikRepository korisnikRepository) {
        this.korisnikRepository = korisnikRepository;
    }


    @GetMapping
    public CollectionModel<EntityModel<Korisnik>> all() {
        List<Korisnik> korisnici = korisnikRepository.findAll();

        List<EntityModel<Korisnik>> entityModels = korisnici.stream()
                .map(korisnik -> EntityModel.of(korisnik,
                        linkTo(methodOn(KorisnikKontroler.class).one(korisnik.getId())).withSelfRel(),
                        linkTo(methodOn(KorisnikKontroler.class).all()).withRel("korisnici")))
                .collect(Collectors.toList());

        return CollectionModel.of(entityModels, linkTo(methodOn(KorisnikKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Korisnik> one(@PathVariable Integer id) {
        Korisnik korisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new KorisnikNotFoundException(id));

        return EntityModel.of(korisnik,
                linkTo(methodOn(KorisnikKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(KorisnikKontroler.class).all()).withRel("korisnici"));
    }

    @PostMapping
    public ResponseEntity<?> newKorisnik(@Valid @RequestBody Korisnik newKorisnik) {
        Korisnik savedKorisnik = korisnikRepository.save(newKorisnik);

        EntityModel<Korisnik> entityModel = EntityModel.of(savedKorisnik,
                linkTo(methodOn(KorisnikKontroler.class).one(savedKorisnik.getId())).withSelfRel(),
                linkTo(methodOn(KorisnikKontroler.class).all()).withRel("korisnici"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceKorisnik(@Valid @RequestBody Korisnik newKorisnik, @PathVariable Integer id) {
        return korisnikRepository.findById(id)
                .map(korisnik -> {
                    korisnik.setIme(newKorisnik.getIme());
                    korisnik.setPrezime(newKorisnik.getPrezime());
                    korisnik.setEmail(newKorisnik.getEmail());
                    korisnik.setLozinka(newKorisnik.getLozinka());
                    korisnik.setBrojTelefona(newKorisnik.getBrojTelefona());
                    korisnik.setAdresa(newKorisnik.getAdresa());
                    korisnik.setRoleId(newKorisnik.getRoleId());
                    korisnik.setKorisnikUuid(newKorisnik.getKorisnikUuid());
                    return ResponseEntity.ok(korisnikRepository.save(korisnik));
                })
                .orElseThrow(() -> new KorisnikNotFoundException(id));
    }



@DeleteMapping("/{id}")
public ResponseEntity<?> deleteKorisnik(@PathVariable Integer id) {
    if (korisnikRepository.existsById(id)) {
        korisnikRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    } else {
        throw new KorisnikNotFoundException(id);
    }
}
}