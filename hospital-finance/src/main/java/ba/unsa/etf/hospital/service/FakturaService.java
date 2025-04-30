package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.repository.FakturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FakturaService {
   // private ObjectMapper objectMapper = new ObjectMapper();
    private final FakturaRepository fakturaRepository;

    public FakturaService(FakturaRepository fakturaRepository) {
        this.fakturaRepository = fakturaRepository;
    }

/*
    @Autowired
    public FakturaService(FakturaRepository fakturaRepository, ObjectMapper objectMapper) {
        this.fakturaRepository = fakturaRepository;
        this.objectMapper = objectMapper;
    }*/

    public List<Faktura> getAllFakture() {
        return fakturaRepository.findAll();
    }
    @Transactional
    public List<Faktura> saveBatchFakture(List<Faktura> fakture) {
        List<Faktura> saved = new ArrayList<>();
        for (Faktura f : fakture) {
            // neka custom validacija ili obrada prije spremanja
            if (f.getIznos() > 0) {
                f.setStatus("neplaćeno");
                saved.add(fakturaRepository.save(f));
            }
        }
        return saved;
    }
    public Faktura saveFaktura(Faktura faktura) {
        return fakturaRepository.save(faktura);
    }

    public Optional<Faktura> findById(Long id) {
        return fakturaRepository.findById(id);
    }

    public void deleteById(Long id) {
        fakturaRepository.findById(id)
                .orElseThrow(() -> new FakturaNotFoundException(id));
        fakturaRepository.deleteById(id);
    }
/*
    public Faktura applyPatchToFaktura(Long id, JsonPatch patch)
            throws JsonPatchException, JsonProcessingException {

        Faktura faktura = fakturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faktura nije pronađena"));

        JsonNode fakturaNode = objectMapper.convertValue(faktura, JsonNode.class);
        JsonNode patched = patch.apply(fakturaNode);
        Faktura patchedFaktura = objectMapper.treeToValue(patched, Faktura.class);

        return fakturaRepository.save(patchedFaktura);
    }*/
}
