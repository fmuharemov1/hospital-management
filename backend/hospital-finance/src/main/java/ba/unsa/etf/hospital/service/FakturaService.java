package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.client.TerminiClient;
import ba.unsa.etf.hospital.exception.FakturaNotFoundException;
import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.repository.FakturaRepository;
import ba.unsa.etf.hospital.model.PaymentRollbackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FakturaService {

    private final FakturaRepository fakturaRepository;
    private final TerminiClient terminiClient;
    private static final Logger log = LoggerFactory.getLogger(FakturaService.class);

    public FakturaService(FakturaRepository fakturaRepository, TerminiClient terminiClient) {
        this.fakturaRepository = fakturaRepository;
        this.terminiClient = terminiClient;
    }

    public List<Faktura> getAllFakture() {
        return fakturaRepository.findAll();
    }

    @Transactional
    public List<Faktura> saveBatchFakture(List<Faktura> fakture) {
        List<Faktura> saved = new ArrayList<>();
        for (Faktura f : fakture) {
            if (f.getIznos() > 0) {
                f.setStatus("neplaćeno");
                saved.add(fakturaRepository.save(f));
            }
        }
        return saved;
    }

    private boolean isTerminZavrsen(Long terminId) {
        try {
            return terminiClient.isTerminObavljen(terminId);
        } catch (Exception e) {
            throw new RuntimeException("Greška prilikom provjere termina", e);
        }
    }

    public Faktura saveFaktura(Faktura faktura) {
        // Ako želiš provjeru termina, otkomentiraj
        /*
        if (!isTerminZavrsen(faktura.getTerminId())) {
            throw new IllegalStateException("Termin nije obavljen. Faktura se ne može kreirati.");
        }
        */
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
    public List<Faktura> getFaktureByKorisnikId(Long korisnikId) {
        return fakturaRepository.findByKorisnikId(korisnikId);
    }

    public List<Faktura> getFaktureByTerminId(Long terminId) {
        return fakturaRepository.findByTerminId(terminId);
    }

    // ✅ Dodano za rollback
    @RabbitListener(queues = "payment.rollback.queue")
    public void handleRollback(PaymentRollbackEvent event) {
        if (event.getFakturaId() != null) {
            fakturaRepository.deleteById(event.getFakturaId());
            log.info("🗑️ Rollback: obrisana faktura ID {}", event.getFakturaId());
        } else {
            log.warn("⚠️ RollbackEvent bez fakturaId");
        }
    }
}
