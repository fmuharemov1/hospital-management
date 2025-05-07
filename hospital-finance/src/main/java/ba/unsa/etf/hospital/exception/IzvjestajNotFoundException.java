package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IzvjestajNotFoundException extends RuntimeException {
    public IzvjestajNotFoundException(Long id) {

        super("Izvještaj sa ID "+ id + " nije pronađen.");
    }
}
