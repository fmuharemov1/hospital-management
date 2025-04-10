package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TerminNotFoundException extends RuntimeException {
    public TerminNotFoundException(Long id) {
        super("Termin sa ID "+ id + " nije pronađen.");
    }
}
