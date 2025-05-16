package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Uloga sa ID "+ id + " nije pronađena.");
    }
}
