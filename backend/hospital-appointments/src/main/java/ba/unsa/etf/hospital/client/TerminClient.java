package ba.unsa.etf.hospital.client;

import ba.unsa.etf.hospital.dto.TerminDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "HOSPITAL-APPOINTMENTS")

public interface TerminClient {

    @GetMapping("/termini/{id}")
    TerminDTO getTermin(@PathVariable("id") Long id);

    @GetMapping("/termini")
    List<TerminDTO> getAll();
}
