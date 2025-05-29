package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FakturaNotFoundException extends RuntimeException {
    public FakturaNotFoundException(Long id) {

        super("Faktura sa ID "+ id + " nije pronađena.");
    }
}
