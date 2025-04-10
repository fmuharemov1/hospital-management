package com.example.elektronski_karton_servis.Exception;



public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(Integer id) {
        super("Uloga sa ID " + id + " nije pronađena.");
    }
}




