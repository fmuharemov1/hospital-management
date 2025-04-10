/*package com.example.elektronski_karton_servis.config;

import com.example.elektronski_karton_servis.model.Korisnik;
import com.example.elektronski_karton_servis.model.Termin;
import com.example.elektronski_karton_servis.Repository.KorisnikRepository;
import com.example.elektronski_karton_servis.Repository.TerminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataLoaderConfig {

    @Autowired
    private TerminRepository terminRepository;

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Bean
    CommandLineRunner dataLoader() {
        return args -> {
            UUID pacijent1Uuid = UUID.fromString("660e8400-e29b-41d4-a716-446655440011");
            UUID osoblje1Uuid = UUID.fromString("770e8400-e29b-41d4-a716-446655440021");
            UUID pacijent2Uuid = UUID.fromString("660e8400-e29b-41d4-a716-446655440012");
            UUID osoblje2Uuid = UUID.fromString("770e8400-e29b-41d4-a716-446655440022");

            Korisnik pacijent1 = korisnikRepository.findByKorisnikUuid(pacijent1Uuid).orElse(null);
            Korisnik osoblje1 = korisnikRepository.findByKorisnikUuid(osoblje1Uuid).orElse(null);
            Korisnik pacijent2 = korisnikRepository.findByKorisnikUuid(pacijent2Uuid).orElse(null);
            Korisnik osoblje2 = korisnikRepository.findByKorisnikUuid(osoblje2Uuid).orElse(null);

            if (pacijent1 != null && osoblje1 != null) {
                Termin termin1 = new Termin();
                termin1.setPacijentUuid(pacijent1.getId()); // Koristite ID korisnika (Integer)
                termin1.setOsobljeUuid(osoblje1.getId());   // Koristite ID korisnika (Integer)
                termin1.setDatumVrijeme(LocalDateTime.of(2024, 4, 1, 10, 0));
                terminRepository.save(termin1);
                System.out.println("Kreiran termin za pacijenta ID: " + pacijent1.getId() + ", osoblje ID: " + osoblje1.getId());
            } else {
                System.err.println("Upozorenje: Pacijent ili osoblje za termin nisu pronađeni. Termin nije kreiran.");
            }

            if (pacijent2 != null && osoblje2 != null) {
                Termin termin2 = new Termin();
                termin2.setPacijentUuid(pacijent2.getId()); // Koristite ID korisnika (Integer)
                termin2.setOsobljeUuid(osoblje2.getId());   // Koristite ID korisnika (Integer)
                termin2.setDatumVrijeme(LocalDateTime.of(2024, 4, 2, 14, 30));
                terminRepository.save(termin2);
                System.out.println("Kreiran termin za pacijenta ID: " + pacijent2.getId() + ", osoblje ID: " + osoblje2.getId());
            } else {
                System.err.println("Upozorenje: Pacijent ili osoblje za termin nisu pronađeni. Termin nije kreiran.");
            }
        };
    }
}
*/
