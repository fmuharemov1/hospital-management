package com.example.elektronski_karton_servis.Exception;


public class TerapijaNotFoundException extends RuntimeException {
    public TerapijaNotFoundException(Integer id) {
        super("Terapija sa ID " + id + " nije pronađena.");
    }
}




