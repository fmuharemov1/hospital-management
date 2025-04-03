package ba.unsa.etf.hospital.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TerminNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String terminNotFoundHandler(TerminNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(FakturaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String fakturaNotFoundHandler(FakturaNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(IzvjestajNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String izvjestajNotFoundHandler(IzvjestajNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(KorisnikNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String korisnikNotFoundHandler(KorisnikNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String roleNotFoundHandler(RoleNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)  // Generalni handler za sve neočekivane greške
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String globalExceptionHandler(Exception ex) {
        return "Došlo je do greške: " + ex.getMessage();
    }
}
