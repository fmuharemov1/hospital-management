package com.example.client_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class DijagnozaDto {
    private Long id;
    private LocalDateTime datumDijagnoze;
    private String naziv;
    private String opis;
    private Long kartonId;
    private Long osobljeUuid;

    // Lista terapija za ovu dijagnozu
    private List<TerapijaDto> terapije = new ArrayList<>();

    // Konstruktori
    public DijagnozaDto() {}

    // Getters/setters - SVI!
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDatumDijagnoze() { return datumDijagnoze; }
    public void setDatumDijagnoze(LocalDateTime datumDijagnoze) { this.datumDijagnoze = datumDijagnoze; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    // ISPRAVKA: vratiti Long umesto Object!
    public Long getKartonId() { return kartonId; }
    public void setKartonId(Long kartonId) { this.kartonId = kartonId; }

    public Long getOsobljeUuid() { return osobljeUuid; }
    public void setOsobljeUuid(Long osobljeUuid) { this.osobljeUuid = osobljeUuid; }

    public List<TerapijaDto> getTerapije() { return terapije; }
    public void setTerapije(List<TerapijaDto> terapije) { this.terapije = terapije; }

    @Override
    public String toString() {
        return "DijagnozaDto{" +
                "id=" + id +
                ", naziv='" + naziv + '\'' +
                ", opis='" + opis + '\'' +
                ", datumDijagnoze=" + datumDijagnoze +
                ", kartonId=" + kartonId +
                ", terapije=" + (terapije != null ? terapije.size() : 0) + " items" +
                '}';
    }
}