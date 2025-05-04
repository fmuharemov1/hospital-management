package com.example.elektronski_karton_servis.Servis;

import com.example.elektronski_karton_servis.Exception.KorisnikNotFoundException;
import com.example.elektronski_karton_servis.model.Korisnik;
import com.example.elektronski_karton_servis.Repository.KorisnikRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class KorisnikServis {

    private final KorisnikRepository korisnikRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Autowired
    public KorisnikServis(KorisnikRepository korisnikRepository, ObjectMapper objectMapper, Validator validator) {
        this.korisnikRepository = korisnikRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public List<Korisnik> getAllKorisnici() {
        return korisnikRepository.findAll();
    }

    public Optional<Korisnik> findById(Integer id) {
        return korisnikRepository.findById(id);
    }

    public Korisnik saveKorisnik(Korisnik korisnik) {
        return korisnikRepository.save(korisnik);
    }

    public void deleteById(Integer id) {
        korisnikRepository.deleteById(id);
    }

    // 1. Patch Metoda
    public Korisnik applyPatchToKorisnik(Integer id, JsonPatch patch) throws JsonPatchException {
        Optional<Korisnik> postojećiKorisnikOptional = korisnikRepository.findById(id);
        if (postojećiKorisnikOptional.isEmpty()) {
            throw new KorisnikNotFoundException(id);
        }
        Korisnik postojećiKorisnik = postojećiKorisnikOptional.get();
        Korisnik patchedKorisnik = null;
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(postojećiKorisnik, JsonNode.class));
            patchedKorisnik = objectMapper.treeToValue(patched, Korisnik.class);
        } catch (JsonPatchException e) {
            System.err.println("Greška prilikom primjene JsonPatch-a: " + e.getMessage());
            e.printStackTrace();
            throw new JsonPatchException("Neuspješno primjenjivanje patch-a (JsonPatch): " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Greška prilikom primjene patch-a (IllegalArgument): " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (JsonProcessingException e) {
            System.err.println("Greška prilikom obrade JSON-a: " + e.getMessage());
            e.printStackTrace();
            throw new JsonPatchException("Neuspješno primjenjivanje patch-a (JSON): " + e.getMessage());
        }
        patchedKorisnik.setId(postojećiKorisnik.getId());

        Set<ConstraintViolation<Korisnik>> violations = validator.validate(patchedKorisnik);
        if (!violations.isEmpty()) {
            String errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Neuspješna validacija nakon patch-a: " + errors);
        }

        try {
            return korisnikRepository.save(patchedKorisnik);
        } catch (Exception e) {
            System.err.println("Greška prilikom spremanja korisnika: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-bacite iznimku da je kontroler može uhvatiti
        }
    }

    // 2. Paginacija i Sortiranje
    public Page<Korisnik> getAllKorisniciPaged(Pageable pageable) {
        return korisnikRepository.findAll(pageable);
    }

    // 3. Custom Upiti
    public List<Korisnik> findByImePrezime(String ime, String prezime) {
        return korisnikRepository.findByImeIgnoreCaseAndPrezimeIgnoreCase(ime, prezime);
    }

    public List<Korisnik> findByImeIliPrezimeSadrzi(String tekst) {
        return korisnikRepository.findByImeIliPrezimeSadrziIgnoreCase(tekst);
    }

    public List<Korisnik> findByUloga(Integer roleId) {
        return korisnikRepository.findByRoleId(roleId);
    }
}