package com.example.client_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoadTestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/send-requests")
    public Map<String, Integer> sendRequests() {
        Map<String, Integer> results = new HashMap<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            String response = restTemplate.getForObject("http://elektronski-karton-servis/instance", String.class);
            results.put(response, results.getOrDefault(response, 0) + 1);
        }

        long end = System.currentTimeMillis();
        results.put("Time(ms)", (int) (end - start));

        return results;
    }
}
