package com.example.elektronski_karton_servis.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage()) // Prijateljski format
                .collect(Collectors.toList());
        String errorMessage = String.join(", ", errors);
        return new ErrorResponse("Invalid", errorMessage, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(TerminNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse terminNotFoundHandler(TerminNotFoundException ex) {
        return new ErrorResponse("Termin not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(DijagnozaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse dijagnozaNotFoundHandler(DijagnozaNotFoundException ex) {
        return new ErrorResponse("Dijagnoza not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(KartonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse kartonNotFoundHandler(KartonNotFoundException ex) {
        return new ErrorResponse("Karton not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(TerapijaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse terapijaNotFoundHandler(TerapijaNotFoundException ex) {
        return new ErrorResponse("Terapija not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(KorisnikNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse korisnikNotFoundHandler(KorisnikNotFoundException ex) {
        return new ErrorResponse("Korisnik not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse roleNotFoundHandler(RoleNotFoundException ex) {
        return new ErrorResponse("Role not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(Exception.class)  // Generalni handler za sve neočekivane greške
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse globalExceptionHandler(Exception ex) {
        return new ErrorResponse("Internal Server Error", "Došlo je do greške: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}