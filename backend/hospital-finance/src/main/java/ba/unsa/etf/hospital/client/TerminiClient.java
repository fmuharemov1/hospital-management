package ba.unsa.etf.hospital.client;

import ba.unsa.etf.hospital.model.Termin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "hospital-appointments")
public interface TerminiClient {

    @GetMapping("/termini/{id}/status")
    boolean isTerminObavljen(@PathVariable("id") Long id);
    @GetMapping("/termini")
    List<Termin> getAllTermini();
}