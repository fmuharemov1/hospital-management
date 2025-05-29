package com.example.elektronski_karton_servis.Controller;


import com.example.elektronski_karton_servis.Exception.RoleNotFoundException;
import com.example.elektronski_karton_servis.model.Role;
import com.example.elektronski_karton_servis.Repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/roles")

public class RoleKontroler {

    private final RoleRepository roleRepository;

    public RoleKontroler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public CollectionModel<EntityModel<Role>> all() {
        List<EntityModel<Role>> roles = roleRepository.findAll().stream()
                .map(role -> EntityModel.of(role,
                        linkTo(methodOn(RoleKontroler.class).one(role.getId())).withSelfRel(),
                        linkTo(methodOn(RoleKontroler.class).all()).withRel("roles")))
                .collect(Collectors.toList());

        return CollectionModel.of(roles, linkTo(methodOn(RoleKontroler.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Role> one(@PathVariable Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));

        return EntityModel.of(role,
                linkTo(methodOn(RoleKontroler.class).one(id)).withSelfRel(),
                linkTo(methodOn(RoleKontroler.class).all()).withRel("roles"));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Role>> newRole(@Valid @RequestBody Role novaRole) {
        Role sacuvanaRole = roleRepository.save(novaRole);

        EntityModel<Role> entityModel = EntityModel.of(sacuvanaRole,
                linkTo(methodOn(RoleKontroler.class).one(sacuvanaRole.getId())).withSelfRel(),
                linkTo(methodOn(RoleKontroler.class).all()).withRel("roles"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> replaceRole(@Valid @RequestBody Role novaRole, @PathVariable Integer id) {
        return roleRepository.findById(id)
                .map(role -> {
                    role.setTipKorisnika(novaRole.getTipKorisnika());
                    role.setSmjena(novaRole.getSmjena());
                    role.setOdjeljenje(novaRole.getOdjeljenje());
                    return ResponseEntity.ok(roleRepository.save(role)); // 200 OK - uspješno ažurirano
                })
                .orElseThrow(() -> new RoleNotFoundException(id)); // 404 Not Found ako ne postoji
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@Valid @PathVariable Integer id) {
        if (roleRepository.existsById(id)) {
            try {
                roleRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Uloga se ne može izbrisati jer je povezana s korisnicima.");
            }
        } else {
            throw new RoleNotFoundException(id);
        }
    }
}