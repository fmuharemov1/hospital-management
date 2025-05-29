package com.example.elektronski_karton_servis.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test-actions")
public class ActionTestController {

    @GetMapping
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("GET log sent");
    }

    @PostMapping
    public ResponseEntity<String> postTest() {
        return ResponseEntity.ok("POST log sent");
    }

    @PutMapping
    public ResponseEntity<String> putTest() {
        return ResponseEntity.ok("PUT log sent");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteTest() {
        return ResponseEntity.ok("DELETE log sent");
    }
}
