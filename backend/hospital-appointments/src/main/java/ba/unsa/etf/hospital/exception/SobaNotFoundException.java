package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SobaNotFoundException extends RuntimeException {
    public SobaNotFoundException(Long id) {
        super("Soba sa ID " + id + " nije pronađena.");
    }
}