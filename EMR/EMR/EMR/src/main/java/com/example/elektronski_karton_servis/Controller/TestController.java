package com.example.elektronski_karton_servis.Controller;

import com.example.elektronski_karton_servis.client.TerminClient;
import com.example.elektronski_karton_servis.dto.TerminDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")  // ← Obavezno
public class TestController {

    private final TerminClient terminClient;

    public TestController(TerminClient terminClient) {
        this.terminClient = terminClient;
    }

    @GetMapping("/termini")  // ← ovo mora odgovarati
    public List<TerminDTO> getTermini() {
        return terminClient.getAll();
    }
}
