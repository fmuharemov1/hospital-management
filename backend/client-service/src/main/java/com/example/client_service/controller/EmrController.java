package com.example.client_service.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/client/emr")
public class EmrController {

    private final RestTemplate restTemplate;
    private final String EKS_SERVICE_URL = "http://localhost:8093/api/kartoni";

    public EmrController (RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients(Authentication authentication) {
        return restTemplate.getForEntity(EKS_SERVICE_URL + "/patients", Object.class);
    }

    @GetMapping("/patients/{patientId}/karton")
    public ResponseEntity<?> getPatientKarton(@PathVariable Long patientId, Authentication authentication) {
        return restTemplate.getForEntity(EKS_SERVICE_URL + "/patient/" + patientId, Object.class);
    }

    @PostMapping("/patients/{patientId}/karton")
    public ResponseEntity<?> addKartonEntry(@PathVariable Long patientId, @RequestBody Object kartonData, Authentication authentication) {
        return restTemplate.postForEntity(EKS_SERVICE_URL, kartonData, Object.class);
    }

    @PutMapping("/kartoni/{kartonId}")
    public ResponseEntity<?> updateKartonEntry(@PathVariable Long kartonId, @RequestBody Object kartonData, Authentication authentication) {
        restTemplate.put(EKS_SERVICE_URL + "/" + kartonId, kartonData);
        return ResponseEntity.ok().build();
    }
}