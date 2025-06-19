package com.example.client_service.controller;

import com.example.client_service.dto.KorisnikDto;
import com.example.client_service.dto.PatientDto;
import com.example.client_service.dto.KartonEntryDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

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
            // Poziva /api/korisnici endpoint na EMR servisu
            ResponseEntity<List<KorisnikDto>> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/korisnici",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<KorisnikDto>>() {}
            );

            System.out.println("EmrController: EMR response status: " + response.getStatusCode());
            System.out.println("EmrController: EMR response body size: " + (response.getBody() != null ? response.getBody().size() : "null"));

            // Konvertuj Korisnik u PatientDto
            List<PatientDto> patients = response.getBody().stream()
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
     *//*
    @GetMapping("/patients/{patientId}/karton")
    public ResponseEntity<List<KartonEntryDto>> getPatientKarton(@PathVariable Long patientId, Authentication authentication) {
        System.out.println("EmrController: Received request for karton of patient " + patientId + " from user: " + authentication.getName());
        try {
            // Poziva /api/kartoni endpoint na EMR servisu
            ResponseEntity<List<KartonEntryDto>> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/kartoni", // Dohvati sve kartone
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<KartonEntryDto>>() {}
            );

            // Filtriraj kartone za određenog pacijenta
            List<KartonEntryDto> patientKartoni = response.getBody().stream()
                    .filter(karton -> karton.getPatientId().equals(patientId))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(patientKartoni);
        } catch (Exception e) {
            System.err.println("EmrController: Error fetching karton for patient " + patientId + " from EMR service: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Dodaje novi unos u medicinski karton.
     */
    /*
    @PostMapping("/patients/{patientId}/karton")
    public ResponseEntity<KartonEntryDto> addKartonEntry(@PathVariable Long patientId, @RequestBody KartonEntryDto kartonData, Authentication authentication) {
        System.out.println("EmrController: Received request to add karton entry for patient " + patientId + " from user: " + authentication.getName());
        try {
            kartonData.setPatientId(patientId);
            ResponseEntity<KartonEntryDto> response = restTemplate.postForEntity(
                    EMR_SERVICE_URL + "/api/kartoni",
                    kartonData,
                    KartonEntryDto.class
            );
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            System.err.println("EmrController: Error adding karton entry for patient " + patientId + ": " + e.getMessage());
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
}