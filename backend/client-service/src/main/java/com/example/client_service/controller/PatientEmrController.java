package com.example.client_service.controller;

import com.example.client_service.dto.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import com.example.client_service.dto.TerapijaDto;
import com.example.client_service.dto.KartonEntryDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/client/patient-emr")
public class PatientEmrController {
    private final RestTemplate restTemplate;
    private final String EMR_SERVICE_URL = "http://localhost:8082";

    public PatientEmrController(@Qualifier("restTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        System.out.println("=== PatientEmrController INITIALIZED ===");
    }

    /**
     * Dohvaća ID trenutno prijavljenog pacijenta
     */
    private Long getCurrentPatientId(Authentication authentication) {
        try {
            // KORISTITE EMAIL UMESTO USERNAME!
            String currentEmail = getCurrentUserEmail(authentication);
            System.out.println("=== DEBUG: Looking for email: " + currentEmail);

            ResponseEntity<HateoasKorisnikResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/korisnici",
                    HttpMethod.GET,
                    null,
                    HateoasKorisnikResponse.class
            );

            System.out.println("=== DEBUG: EMR Response status: " + response.getStatusCode());

            HateoasKorisnikResponse hateoasResponse = response.getBody();
            List<KorisnikDto> users = hateoasResponse != null ? hateoasResponse.getKorisnici() : null;

            System.out.println("=== USERS COUNT: " + (users != null ? users.size() : 0));

            if (users != null) {
                KorisnikDto currentUser = users.stream()
                        .filter(user -> {
                            boolean emailMatch = user.getEmail() != null && user.getEmail().equals(currentEmail);
                            System.out.println("=== Checking: " + user.getEmail() + " vs " + currentEmail);
                            return emailMatch;
                        })
                        .findFirst()
                        .orElse(null);

                System.out.println("=== FOUND USER: " + (currentUser != null ? currentUser.getId() : "NOT FOUND"));
                return currentUser != null ? currentUser.getId().longValue() : null;
            }

            return null;

        } catch (Exception e) {
            System.err.println("Error getting current patient ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Dohvaća podatke trenutno prijavljenog pacijenta
     * GET /api/client/patient-emr/me
     */
    @GetMapping("/me")
    public ResponseEntity<PatientDto> getCurrentPatient(Authentication authentication) {
        System.out.println("=== PatientEmrController: GET /me ===");
        System.out.println("User: " + authentication.getName());

        try {
            // UMESTO username-a, koristite email iz JWT token-a!
            String currentEmail = getCurrentUserEmail(authentication);
            System.out.println("=== Looking for email: " + currentEmail);

            // Pozovi EMR servis za podatke o korisnicima
            ResponseEntity<HateoasKorisnikResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/korisnici",
                    HttpMethod.GET,
                    null,
                    HateoasKorisnikResponse.class
            );

            PatientDto patient = convertToPatientDto(response.getBody(), currentEmail);

            if (patient == null) {
                System.out.println("=== Patient is NULL - returning 404");
                return ResponseEntity.status(404).body(null);
            }

            System.out.println("=== Returning patient: " + patient.getFullName());
            return ResponseEntity.ok(patient);

        } catch (Exception e) {
            System.err.println("Error getting current patient: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // DODAJTE OVU METODU:
    private String getCurrentUserEmail(Authentication authentication) {
        // Ako koristite JWT, možda možete pristupiti email-u direktno
        // Alternativno, možete mapirati username na email
        String username = authentication.getName();

        // ZA SADA - hard-coded mapping
        if ("hbeslic".equals(username)) {
            return "hbeslic@gmail.com";
        }

        return username; // fallback
    }
    @PostConstruct
    public void showMappings() {
        System.out.println("=== PatientEmrController ENDPOINTS ===");
        System.out.println("/api/client/patient-emr/me");
        System.out.println("/api/client/patient-emr/history");
        System.out.println("/api/client/patient-emr/test");
    }
    /**
     * Dohvaća karton trenutno prijavljenog pacijenta
     * GET /api/client/patient-emr/history
     */
    /**
     * Dohvaća karton trenutno prijavljenog pacijenta
     * GET /api/client/patient-emr/history
     */
    @GetMapping("/history")
    public ResponseEntity<List<KartonEntryDto>> getCurrentPatientHistory(Authentication authentication) {
        System.out.println("=== PatientEmrController: GET /history ===");
        System.out.println("User: " + authentication.getName());

        try {
            Long patientId = getCurrentPatientId(authentication);
            if (patientId == null) {
                return ResponseEntity.status(404).body(List.of());
            }

            System.out.println("Getting history for patient ID: " + patientId);

            // 1. Dohvati sve kartone
            ResponseEntity<HateoasKartonResponse> kartonResponse = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/kartoni",
                    HttpMethod.GET,
                    null,
                    HateoasKartonResponse.class
            );

            List<KartonEntryDto> allKartoni = kartonResponse.getBody() != null ?
                    kartonResponse.getBody().getKartoni() : new ArrayList<>();

            System.out.println("=== Total kartoni found: " + allKartoni.size());

            // 2. Filtriraj kartone za trenutnog pacijenta
            List<KartonEntryDto> patientKartoni = allKartoni.stream()
                    .filter(karton -> karton.getPacijentUuid().equals(patientId))
                    .collect(Collectors.toList());

            System.out.println("=== Patient kartoni: " + patientKartoni.size());

            // 3. Za svaki karton, dohvati dijagnoze
            for (KartonEntryDto karton : patientKartoni) {
                List<DijagnozaDto> dijagnoze = getDijagnozeForKarton(karton.getId());
                karton.setDijagnoze(dijagnoze);

                // 4. Za svaku dijagnozu, dohvati terapije
                for (DijagnozaDto dijagnoza : dijagnoze) {
                    List<TerapijaDto> terapije = getTerapijeForDijagnoza(dijagnoza.getId());
                    dijagnoza.setTerapije(terapije);
                }
            }

            return ResponseEntity.ok(patientKartoni);

        } catch (Exception e) {
            System.err.println("Error getting patient history: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(List.of());
        }
    }

    // Dodajte helper metode:
    private List<DijagnozaDto> getDijagnozeForKarton(Long kartonId) {
        try {
            // Pozovi EMR Service za dijagnoze
            ResponseEntity<HateoasDijagnozaResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/dijagnoze",
                    HttpMethod.GET,
                    null,
                    HateoasDijagnozaResponse.class
            );

            List<DijagnozaDto> allDijagnoze = response.getBody() != null ?
                    response.getBody().getDijagnoze() : new ArrayList<>();

            // Filtriraj po karton ID
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
            // Pozovi EMR Service za terapije
            ResponseEntity<HateoasTerapijaResponse> response = restTemplate.exchange(
                    EMR_SERVICE_URL + "/api/terapije",
                    HttpMethod.GET,
                    null,
                    HateoasTerapijaResponse.class
            );

            List<TerapijaDto> allTerapije = response.getBody() != null ?
                    response.getBody().getTerapije() : new ArrayList<>();

            // Filtriraj po dijagnoza ID
            return allTerapije.stream()
                    .filter(terapija -> terapija.getDijagnozaId().equals(dijagnozaId))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Error getting terapije for dijagnoza " + dijagnozaId + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("=== PatientEmrController /test CALLED ===");
        return ResponseEntity.ok("PatientEmrController is working!");
    }
    /**
     * Alternativno - direktno koristi postojeći EmrController endpoint
     * GET /api/client/patient-emr/patients/me/karton
     */
    @GetMapping("/patients/me/karton")
    public ResponseEntity<List<KartonEntryDto>> getCurrentPatientKarton(Authentication authentication) {
        System.out.println("=== PatientEmrController: GET /patients/me/karton ===");

        try {
            Long patientId = getCurrentPatientId(authentication);
            if (patientId == null) {
                return ResponseEntity.status(404).body(List.of());
            }

            // Delegiraj na postojeći EmrController
            // Pozovi Client Service EmrController endpoint
            ResponseEntity<List<KartonEntryDto>> response = restTemplate.exchange(
                    "http://localhost:8092/api/client/emr/patients/" + patientId + "/karton",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<KartonEntryDto>>() {}
            );

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {
            System.err.println("Error getting patient karton: " + e.getMessage());
            return ResponseEntity.status(500).body(List.of());
        }
    }

    private PatientDto convertToPatientDto(HateoasKorisnikResponse hateoasResponse, String email) {
        System.out.println("=== convertToPatientDto called with email: " + email);

        List<KorisnikDto> users = hateoasResponse.getKorisnici();
        if (users == null || users.isEmpty()) {
            System.out.println("=== No users found in response");
            return null;
        }

        System.out.println("=== Searching through " + users.size() + " users:");
        for (KorisnikDto user : users) {
            System.out.println("=== Checking user: " + user.getEmail() + " vs looking for: " + email);
        }

        // Nađi korisnika po email-u
        KorisnikDto korisnik = users.stream()
                .filter(user -> {
                    boolean match = user.getEmail() != null && user.getEmail().equals(email);
                    System.out.println("=== Email match for " + user.getEmail() + ": " + match);
                    return match;
                })
                .findFirst()
                .orElse(null);

        if (korisnik == null) {
            System.out.println("=== No matching user found!");
            return null;
        }

        System.out.println("=== Found matching user: " + korisnik.getEmail() + " (ID: " + korisnik.getId() + ")");

        PatientDto patient = new PatientDto();
        patient.setId(korisnik.getId().longValue());
        patient.setFullName(korisnik.getIme() + " " + korisnik.getPrezime());
        patient.setEmail(korisnik.getEmail());

        System.out.println("=== Created PatientDto: " + patient.getFullName());
        return patient;
    }

}