package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Faktura;
import ba.unsa.etf.hospital.model.PaymentEvent;
import ba.unsa.etf.hospital.model.PaymentRollbackEvent;
import ba.unsa.etf.hospital.repository.FakturaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private final FakturaRepository fakturaRepository;
    private final RabbitTemplate rabbitTemplate;

    public PaymentEventListener(FakturaRepository fakturaRepository, RabbitTemplate rabbitTemplate) {
        this.fakturaRepository = fakturaRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "payment.created")
    public void handlePaymentCreated(PaymentEvent event) {
        System.out.println("📥 Primljen event: " + event);

        Faktura faktura = new Faktura();
        faktura.setIznos(event.getIznos());
        faktura.setStatus("NAPLAĆENO");
        faktura.setMetod("KARTICA");
        faktura.setTerminId(event.getTerminId());
        faktura.setKorisnikId(event.getKorisnikId());

        try {
            Faktura saved = fakturaRepository.save(faktura);
            System.out.println("✅ Faktura kreirana!");

            // šaljemo da je uspješno
            rabbitTemplate.convertAndSend("hospital.exchange", "finance.success", event);
        } catch (Exception e) {
            System.out.println("❌ Greška pri kreiranju fakture: " + e.getMessage());

            // šaljemo rollback samo ako faktura ima ID (ako je sačuvana prije greške)
            if (faktura.getId() != null) {
                rabbitTemplate.convertAndSend("hospital.exchange", "finance.failed",
                        new PaymentRollbackEvent(faktura.getId()));
            }
        }
    }
}
