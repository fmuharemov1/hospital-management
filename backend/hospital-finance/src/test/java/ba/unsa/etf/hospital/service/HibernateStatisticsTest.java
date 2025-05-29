package ba.unsa.etf.hospital.service;

import ba.unsa.etf.hospital.model.Korisnik;
import ba.unsa.etf.hospital.repository.KorisnikRepository;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HibernateStatisticsTest {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private KorisnikRepository korisnikRepository;

    private Statistics statistics;

    @BeforeEach
    public void setUp() {
        // Dohvaćanje Hibernate statistike
        statistics = sessionFactory.getStatistics();
        statistics.clear(); // Očistimo prethodne statistike prije svakog testa
    }

    @Test
    public void testNoNPlusOneProblem() {
        // Preuzimanje svih korisnika. Ako nije postavljen 'fetch join', ovo može uzrokovati N+1 problem.
        List<Korisnik> korisnici = korisnikRepository.findAll(); // Ovo bi moglo uzrokovati N+1 ako nismo koristili JOIN.

        // Provjeravamo broj upita koje je Hibernate izvršio
        long queryCount = statistics.getQueryExecutionCount();

        // Provjeravamo da li je broj upita 1, što znači da su svi podaci učitani u jednom upitu
        // Ako je broj upita veći od 1, to bi značilo da postoji N+1 problem
        assertEquals(1, queryCount, "Broj upita mora biti 1, što znači da nije bilo N+1 problema.");
    }
}
