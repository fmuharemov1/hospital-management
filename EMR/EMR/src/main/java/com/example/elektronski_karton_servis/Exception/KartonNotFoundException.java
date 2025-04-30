package com.example.elektronski_karton_servis.Exception;



public class KartonNotFoundException extends RuntimeException {
    public KartonNotFoundException(Integer id) {
        super("Karton sa ID " + id + " nije pronađen.");
    }
}


