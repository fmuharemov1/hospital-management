package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
public class TerapijaDto {
    private Long id;
    private LocalDateTime datumPocetka;
    private LocalDateTime datumZavrsetka;

    @JsonProperty("dijagnoza_id") // ISPRAVKA: mapiranje na snake_case
    private Long dijagnozaId;

    private String naziv;
    private String opis;
    private Long osobljeUuid;

    // Konstruktori
    public TerapijaDto() {}

    // Getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDatumPocetka() { return datumPocetka; }
    public void setDatumPocetka(LocalDateTime datumPocetka) { this.datumPocetka = datumPocetka; }

    public LocalDateTime getDatumZavrsetka() { return datumZavrsetka; }
    public void setDatumZavrsetka(LocalDateTime datumZavrsetka) { this.datumZavrsetka = datumZavrsetka; }

    public Long getDijagnozaId() { return dijagnozaId; }
    public void setDijagnozaId(Long dijagnozaId) { this.dijagnozaId = dijagnozaId; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public Long getOsobljeUuid() { return osobljeUuid; }
    public void setOsobljeUuid(Long osobljeUuid) { this.osobljeUuid = osobljeUuid; }

    @Override
    public String toString() {
        return "TerapijaDto{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", opis='" + opis + '\'' +
                ", datumPocetka=" + datumPocetka +
                ", datumZavrsetka=" + datumZavrsetka +
                ", dijagnozaId=" + dijagnozaId +
                '}';
    }
}