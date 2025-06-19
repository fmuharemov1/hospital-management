package com.example.client_service.dto;

public class KorisnikDto {
    private Integer id;
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private String brojTelefona;
    private String adresa;
    private Integer roleId;
    private Integer korisnikUuid;

    // konstruktori, getteri i setteri
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Object getKorisnickoIme() {
        return korisnikUuid;
    }

    // ostali getteri i setteri...
}