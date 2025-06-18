package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.PaymentEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public void processPayment(PaymentEvent event) {
        // Ovdje ide logika za naplatu, validaciju, evidenciju itd.
        System.out.println("✅ Obrada uplate za korisnika " + event.getKorisnikId());
        System.out.println("💸 Iznos: " + event.getIznos() + " KM");
        System.out.println("📅 Termin ID: " + event.getTerminId());
    }
}
