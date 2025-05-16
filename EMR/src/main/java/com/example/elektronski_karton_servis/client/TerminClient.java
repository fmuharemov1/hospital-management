package com.example.elektronski_karton_servis.client;

import com.example.elektronski_karton_servis.dto.TerminDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "HOSPITAL-APPOINTMENTS")

public interface TerminClient {

    @GetMapping("/termini/{id}")
    TerminDTO getTermin(@PathVariable("id") Long id);

    @GetMapping("/termini")
    List<TerminDTO> getAll();
}
