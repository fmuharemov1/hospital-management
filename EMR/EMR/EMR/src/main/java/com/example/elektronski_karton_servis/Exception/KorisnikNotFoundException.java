package com.example.elektronski_karton_servis.Exception;



public class KorisnikNotFoundException extends RuntimeException {
    public KorisnikNotFoundException(Integer id) {
        super("Korisnik sa ID " + id + " nije pronađen.");
    }
}