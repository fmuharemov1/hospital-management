package com.example.elektronski_karton_servis.Exception;



public class DijagnozaNotFoundException extends RuntimeException {
    public DijagnozaNotFoundException(Integer id) {
        super("Dijagnoza sa ID " + id + " nije pronađena.");
    }
}


