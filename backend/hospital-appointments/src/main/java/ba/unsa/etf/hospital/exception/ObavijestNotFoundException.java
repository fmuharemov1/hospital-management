package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObavijestNotFoundException extends RuntimeException {
    public ObavijestNotFoundException(Long id) {
        super("Obavijest sa ID " + id + " nije pronađena.");
    }
}