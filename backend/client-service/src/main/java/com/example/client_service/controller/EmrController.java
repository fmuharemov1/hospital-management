package com.example.client_service.controller;

import com.example.client_service.dto.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/emr")
public class EmrController {

    private final RestTemplate restTemplate;
    // Ažurirani URL za EMR servis na portu 8082
    private final String EMR_SERVICE_URL = "http://localhost:8082";

    public EmrController(@Qualifier("restTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Dohvaća sve korisnike (pacijente) sa EMR servisa.
     */
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getAllPatients(Authentication authentication) {
        System.out.println("=== EmrController: START getAllPatients ===");
        System.out.println("EmrController: User: " + authentication.getName());
        System.out.println("EmrController: Calling EMR service at: " + EMR_SERVICE_URL + "/api/korisnici");

        try {
            // Pozovi sa EmrResponse wrapper-om
            ResponseEntity<EmrResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/korisnici",
                    HttpMethod.GET,
                    null,
                    EmrResponse.class
            );

            System.out.println("EmrController: EMR response status: " + response.getStatusCode());

            // Izvuci korisnikList iz _embedded strukture
            List<KorisnikDto> korisnici = response.getBody().getEmbedded().getKorisnikList();
            System.out.println("EmrController: EMR response body size: " + korisnici.size());

            // Konvertuj Korisnik u PatientDto
            List<PatientDto> patients = korisnici.stream()
                    .map(this::convertToPatientDto)
                    .collect(Collectors.toList());

            System.out.println("EmrController: Converted to " + patients.size() + " patients");
            System.out.println("=== EmrController: SUCCESS ===");
            return ResponseEntity.ok(patients);

        } catch (Exception e) {
            System.err.println("=== EmrController: ERROR ===");
            System.err.println("EmrController: Error type: " + e.getClass().getSimpleName());
            System.err.println("EmrController: Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Dohvaća medicinski karton za određenog pacijenta.
     */
    @GetMapping("/patients/{patientId}/karton")
    public ResponseEntity<List<KartonEntryDto>> getPatientKarton(@PathVariable Long patientId, Authentication authentication) {
        System.out.println("EmrController: Received request for karton of patient " + patientId + " from user: " + authentication.getName());
        try {
            ResponseEntity<KartonResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/kartoni",
                    HttpMethod.GET,
                    null,
                    KartonResponse.class
            );

            List<KartonEntryDto> allKartoni = response.getBody().getEmbedded().getKartonList();

            // Filtriraj kartone za određenog pacijenta
            List<KartonEntryDto> patientKartoni = allKartoni.stream()
                    .filter(karton -> karton.getPatientId().equals(patientId))
                    .collect(Collectors.toList());

            System.out.println("Filtered kartoni for patient " + patientId + ": " + patientKartoni.size());

            // DODAJ OVO - učitaj dijagnoze i terapije!
            for (KartonEntryDto karton : patientKartoni) {
                System.out.println("Loading dijagnoze for karton: " + karton.getId());
                List<DijagnozaDto> dijagnoze = getDijagnozeForKarton(karton.getId());
                karton.setDijagnoze(dijagnoze);
                System.out.println("Found " + dijagnoze.size() + " dijagnoze");

                for (DijagnozaDto dijagnoza : dijagnoze) {
                    System.out.println("Loading terapije for dijagnoza: " + dijagnoza.getId());
                    List<TerapijaDto> terapije = getTerapijeForDijagnoza(dijagnoza.getId());
                    dijagnoza.setTerapije(terapije);
                    System.out.println("Found " + terapije.size() + " terapije");
                }
            }

            return ResponseEntity.ok(patientKartoni);
        } catch (Exception e) {
            System.err.println("EmrController: Error fetching karton for patient " + patientId + " from EMR service: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    /**
     * Dodaje novi unos u medicinski karton.
     */

    @PostMapping("/patients/{patientId}/karton")
    public ResponseEntity<KartonEntryDto> addKartonEntry(@PathVariable Long patientId, @RequestBody KartonEntryDto kartonData, Authentication authentication) {
        System.out.println("=== EmrController: POST karton entry ===");
        System.out.println("Patient ID: " + patientId);
        System.out.println("User: " + authentication.getName());
        System.out.println("Received data: " + kartonData); // Dodaj toString u KartonEntryDto

        try {
            kartonData.setPatientId(patientId); // ili setPacijentUuid

            System.out.println("Sending to EMR service: " + kartonData);

            ResponseEntity<KartonEntryDto> response = restTemplate.postForEntity(
                    EMR_SERVICE_URL + "/api/kartoni",
                    kartonData,
                    KartonEntryDto.class
            );

            System.out.println("EMR response status: " + response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            System.err.println("EmrController: Error adding karton entry: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
    /**
     * Ažurira postojeći unos u medicinskom kartonu.
     */
    @PutMapping("/kartoni/{kartonId}")
    public ResponseEntity<Void> updateKartonEntry(@PathVariable Long kartonId, @RequestBody KartonEntryDto kartonData, Authentication authentication) {
        System.out.println("EmrController: Received request to update karton entry " + kartonId + " from user: " + authentication.getName());
        try {
            restTemplate.put(EMR_SERVICE_URL + "/api/kartoni/" + kartonId, kartonData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("EmrController: Error updating karton entry " + kartonId + ": " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
    /**
     * Briše unos iz medicinskog kartona.
     */
    @DeleteMapping("/kartoni/{kartonId}")
    public ResponseEntity<Void> deleteKartonEntry(@PathVariable Long kartonId, Authentication authentication) {
        System.out.println("EmrController: Received request to delete karton entry " + kartonId + " from user: " + authentication.getName());
        try {
            restTemplate.delete(EMR_SERVICE_URL + "/api/kartoni/" + kartonId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("EmrController: Error deleting karton entry " + kartonId + ": " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Konvertuje KorisnikDto u PatientDto
     */
    private PatientDto convertToPatientDto(KorisnikDto korisnik) {
        PatientDto patient = new PatientDto();
        patient.setId(korisnik.getId().longValue());
        patient.setFullName(korisnik.getIme() + " " + korisnik.getPrezime());
        patient.setEmail(korisnik.getEmail());
        return patient;
    }
    // DODAJ helper metode na kraj klase, prije zatvorene zagrade
    private List<DijagnozaDto> getDijagnozeForKarton(Long kartonId) {
        try {
            ResponseEntity<HateoasDijagnozaResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/dijagnoze",
                    HttpMethod.GET,
                    null,
                    HateoasDijagnozaResponse.class
            );

            List<DijagnozaDto> allDijagnoze = response.getBody() != null ?
                    response.getBody().getDijagnoze() : new ArrayList<>();

            return allDijagnoze.stream()
                    .filter(dijagnoza -> dijagnoza.getKartonId().equals(kartonId))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error getting dijagnoze for karton " + kartonId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<TerapijaDto> getTerapijeForDijagnoza(Long dijagnozaId) {
        try {
            ResponseEntity<HateoasTerapijaResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/terapije",
                    HttpMethod.GET,
                    null,
                    HateoasTerapijaResponse.class
            );

            List<TerapijaDto> allTerapije = response.getBody() != null ?
                    response.getBody().getTerapije() : new ArrayList<>();

            return allTerapije.stream()
                    .filter(terapija -> terapija.getDijagnozaId().equals(dijagnozaId))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error getting terapije for dijagnoza " + dijagnozaId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    /**
     * Dodaje novu dijagnozu u karton
     */
    @PostMapping("/kartoni/{kartonId}/dijagnoze")
    public ResponseEntity<DijagnozaDto> addDijagnoza(@PathVariable Long kartonId, @RequestBody DijagnozaDto dijagnozaData, Authentication authentication) {
        System.out.println("=== EmrController: POST dijagnoza ===");
        System.out.println("Karton ID: " + kartonId);
        System.out.println("User: " + authentication.getName());
        System.out.println("Received dijagnoza data: " + dijagnozaData);

        try {
            dijagnozaData.setKartonId(kartonId);

            ResponseEntity<DijagnozaDto> response = restTemplate.postForEntity(
                    EMR_SERVICE_URL + "/api/dijagnoze",
                    dijagnozaData,
                    DijagnozaDto.class
            );

            System.out.println("EMR dijagnoza response status: " + response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            System.err.println("EmrController: Error adding dijagnoza: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Dodaje novu terapiju u dijagnozu
     */
    @PostMapping("/dijagnoze/{dijagnozaId}/terapije")
    public ResponseEntity<TerapijaDto> addTerapija(@PathVariable Long dijagnozaId, @RequestBody TerapijaDto terapijaData, Authentication authentication) {
        System.out.println("=== EmrController: POST terapija ===");
        System.out.println("Dijagnoza ID: " + dijagnozaId);
        System.out.println("User: " + authentication.getName());
        System.out.println("Received terapija data: " + terapijaData);

        try {
            terapijaData.setDijagnozaId(dijagnozaId);

            ResponseEntity<TerapijaDto> response = restTemplate.postForEntity(
                    EMR_SERVICE_URL + "/api/terapije",
                    terapijaData,
                    TerapijaDto.class
            );

            System.out.println("EMR terapija response status: " + response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            System.err.println("EmrController: Error adding terapija: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Ažurira dijagnozu
     */
    /**
     * Ažurira dijagnozu
     */
    @PutMapping("/dijagnoze/{dijagnozaId}")
    public ResponseEntity<DijagnozaDto> updateDijagnoza(@PathVariable Long dijagnozaId, @RequestBody DijagnozaDto dijagnozaData, Authentication authentication) {
        System.out.println("=== EmrController: PUT dijagnoza ===");
        System.out.println("Dijagnoza ID: " + dijagnozaId);

        try {
            restTemplate.put(EMR_SERVICE_URL + "/api/dijagnoze/" + dijagnozaId, dijagnozaData);
            return ResponseEntity.ok(dijagnozaData);
        } catch (Exception e) {
            System.err.println("EmrController: Error updating dijagnoza: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Ažurira terapiju
     */
    @PutMapping("/terapije/{terapijaId}")
    public ResponseEntity<TerapijaDto> updateTerapija(@PathVariable Long terapijaId, @RequestBody TerapijaDto terapijaData, Authentication authentication) {
        System.out.println("=== EmrController: PUT terapija ===");
        System.out.println("Terapija ID: " + terapijaId);

        try {
            restTemplate.put(EMR_SERVICE_URL + "/api/terapije/" + terapijaId, terapijaData);
            return ResponseEntity.ok(terapijaData);
        } catch (Exception e) {
            System.err.println("EmrController: Error updating terapija: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Briše dijagnozu
     */
    @DeleteMapping("/dijagnoze/{dijagnozaId}")
    public ResponseEntity<Void> deleteDijagnoza(@PathVariable Long dijagnozaId, Authentication authentication) {
        System.out.println("=== EmrController: DELETE dijagnoza ===");
        System.out.println("Dijagnoza ID: " + dijagnozaId);

        try {
            restTemplate.delete(EMR_SERVICE_URL + "/api/dijagnoze/" + dijagnozaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("EmrController: Error deleting dijagnoza: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Briše terapiju
     */
    @DeleteMapping("/terapije/{terapijaId}")
    public ResponseEntity<Void> deleteTerapija(@PathVariable Long terapijaId, Authentication authentication) {
        System.out.println("=== EmrController: DELETE terapija ===");
        System.out.println("Terapija ID: " + terapijaId);

        try {
            restTemplate.delete(EMR_SERVICE_URL + "/api/terapije/" + terapijaId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("EmrController: Error deleting terapija: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}