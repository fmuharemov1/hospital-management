package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.Exception.KorisnikNotFoundException;
import com.example.elektronski_karton_servis.Servis.KorisnikServis;
import com.example.elektronski_karton_servis.model.Korisnik;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/korisnici")
public class KorisnikKontroler {

    private final KorisnikServis korisnikServis;

    public KorisnikKontroler(KorisnikServis korisnikServis) {
        this.korisnikServis = korisnikServis;
    }

    @GetMapping
    public CollectionModel<EntityModel<Korisnik>> all() {
        List<Korisnik> korisnici = korisnikServis.getAllKorisnici();

        List<EntityModel<Korisnik>> entityModels = korisnici.stream()
                .map(korisnik -> EntityModel.of(korisnik,
                        linkTo(methodOn(KorisnikKontroler.class).one(korisnik.getId())).withSelfRel(),
                        linkTo(methodOn(KorisnikKontroler.class).all()).withRel("korisnici")))
                .collect(Collectors.toList());

        return CollectionModel.of(entityModels, linkTo(methodOn(KorisnikKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Korisnik> one(@PathVariable Integer id) {
        Korisnik korisnik = korisnikServis.findById(id)
                .orElseThrow(() -> new KorisnikNotFoundException(id));

        return EntityModel.of(korisnik,
                linkTo(methodOn(KorisnikKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(KorisnikKontroler.class).all()).withRel("korisnici"));
    }

    @PostMapping
    public ResponseEntity<?> newKorisnik(@Valid @RequestBody Korisnik newKorisnik) {
        Korisnik savedKorisnik = korisnikServis.saveKorisnik(newKorisnik);

        EntityModel<Korisnik> entityModel = EntityModel.of(savedKorisnik,
                linkTo(methodOn(KorisnikKontroler.class).one(savedKorisnik.getId())).withSelfRel(),
                linkTo(methodOn(KorisnikKontroler.class).all()).withRel("korisnici"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceKorisnik(@Valid @RequestBody Korisnik newKorisnik, @PathVariable Integer id) {
        Korisnik updatedKorisnik = korisnikServis.findById(id)
                .map(korisnik -> {
                    newKorisnik.setId(id); // Osigurajte da se ID ne mijenja
                    return korisnikServis.saveKorisnik(newKorisnik);
                })
                .orElseThrow(() -> new KorisnikNotFoundException(id));
        return ResponseEntity.ok(updatedKorisnik);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKorisnik(@PathVariable Integer id) {
        korisnikServis.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<?> patchKorisnik(@PathVariable Integer id, @RequestBody JsonPatch patch) {
        try {
            Korisnik patchedKorisnik = korisnikServis.applyPatchToKorisnik(id, patch);
            return ResponseEntity.ok(patchedKorisnik);
        } catch (KorisnikNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (JsonPatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Invalid patch request: " + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Došlo je do neočekivane greške prilikom obrade patch zahtjeva."));
        }
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Korisnik>> getAllKorisniciPaged(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String[] sortProperties) {

        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Parametri 'page' moraju biti >= 0, a 'size' mora biti > 0.");
        }

        Sort sort = Sort.unsorted();
        if (sortProperties != null && sortProperties.length > 0) {
            for (String sortProperty : sortProperties) {
                String[] parts = sortProperty.split(",");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Neispravan format 'sort' parametra. Treba biti 'polje,smjer' (npr., 'ime,asc').");
                }
                String field = parts[0];
                String direction = parts[1].toLowerCase();
                if (!direction.equals("asc") && !direction.equals("desc")) {
                    throw new IllegalArgumentException("Neispravan smjer sortiranja: '" + direction + "'. Dozvoljeno je 'asc' ili 'desc'.");
                }
                sort = sort.and(Sort.by(Sort.Direction.fromString(direction), field));
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Korisnik> korisnici = korisnikServis.getAllKorisniciPaged(pageable);
        return ResponseEntity.ok(korisnici);
    }

    @GetMapping("/byImePrezime")
    public ResponseEntity<List<Korisnik>> findByImePrezime(@RequestParam String ime, @RequestParam String prezime) {
        List<Korisnik> korisnici = korisnikServis.findByImePrezime(ime, prezime);
        if (korisnici.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(korisnici);
    }

    @GetMapping("/byImeIliPrezimeSadrzi")
    public ResponseEntity<List<Korisnik>> findByImeIliPrezimeSadrzi(@RequestParam String tekst) {
        List<Korisnik> korisnici = korisnikServis.findByImeIliPrezimeSadrzi(tekst);
        if (korisnici.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(korisnici);
    }

    @GetMapping("/byUloga/{roleId}")
    public ResponseEntity<List<Korisnik>> findByUloga(@PathVariable Integer roleId) {
        List<Korisnik> korisnici = korisnikServis.findByUloga(roleId);
        if (korisnici.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(korisnici);
    }
}