package com.example.elektronski_karton_servis.Exception;



public class TerminNotFoundException extends RuntimeException {
    public TerminNotFoundException(Integer id) {
        super("Ne može se pronaći termin sa ID-om " + id);
    }
}



