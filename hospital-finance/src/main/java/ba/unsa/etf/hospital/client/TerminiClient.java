package ba.unsa.etf.hospital.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "termini-service", url = "http://localhost:8081")
public interface TerminiClient {

    @GetMapping("/termini/{id}/status")
    boolean isTerminObavljen(@PathVariable("id") Long id);
}
