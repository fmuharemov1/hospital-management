package com.example.client_service.controller;

import com.example.client_service.dto.IzvjestajDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/client/reports")
public class ReportsController {

    private final RestTemplate restTemplate;
    // URL vašeg hospital finance mikroservisa
    private final String HOSPITAL_FINANCE_URL = "http://localhost:8090"; // ili koji god port

    public ReportsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Dohvaća sve izvještaje sa paginacijom
     */
    @GetMapping
    public ResponseEntity<Page<IzvjestajDto>> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        System.out.println("ReportsController: Getting all reports for user: " + authentication.getName());

        try {
            ResponseEntity<Page<IzvjestajDto>> response = restTemplate.exchange(
                    HOSPITAL_FINANCE_URL + "/izvjestaji?page=" + page + "&size=" + size,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Page<IzvjestajDto>>() {}
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            System.err.println("ReportsController: Error fetching reports: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Filtrira izvještaje po tipu
     */
    @GetMapping("/filter")
    public ResponseEntity<List<IzvjestajDto>> filterReports(
            @RequestParam String tipIzvjestaja,
            @RequestParam(required = false) Integer minBrojPacijenata,
            @RequestParam(required = false) Double minIznos,
            Authentication authentication) {

        System.out.println("ReportsController: Filtering reports by type: " + tipIzvjestaja);

        try {
            String url = HOSPITAL_FINANCE_URL + "/izvjestaji/filter?tipIzvjestaja=" + tipIzvjestaja;

            if (minBrojPacijenata != null) {
                url += "&minBrojPacijenata=" + minBrojPacijenata;
            }
            if (minIznos != null) {
                url += "&minIznos=" + minIznos;
            }

            ResponseEntity<List<IzvjestajDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<IzvjestajDto>>() {}
            );

            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            System.err.println("ReportsController: Error filtering reports: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Dohvaća specifične tipove izvještaja za dropdown
     */
    @GetMapping("/daily")
    public ResponseEntity<List<IzvjestajDto>> getDailyReports(Authentication authentication) {
        return filterReports("daily", null, null, authentication);
    }

    @GetMapping("/weekly")
    public ResponseEntity<List<IzvjestajDto>> getWeeklyReports(Authentication authentication) {
        return filterReports("weekly", null, null, authentication);
    }

    @GetMapping("/monthly")
    public ResponseEntity<List<IzvjestajDto>> getMonthlyReports(Authentication authentication) {
        return filterReports("monthly", null, null, authentication);
    }

    /**
     * Kreira novi izvještaj
     */
    @PostMapping
    public ResponseEntity<IzvjestajDto> createReport(@RequestBody IzvjestajDto izvjestaj, Authentication authentication) {
        System.out.println("ReportsController: Creating new report for user: " + authentication.getName());

        try {
            ResponseEntity<IzvjestajDto> response = restTemplate.postForEntity(
                    HOSPITAL_FINANCE_URL + "/izvjestaji",
                    izvjestaj,
                    IzvjestajDto.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            System.err.println("ReportsController: Error creating report: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}