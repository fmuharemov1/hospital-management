package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IzvjestajDto {
    private Long id;
    private String tipIzvjestaja;
    private Integer brojPacijenata;
    private Integer brojTermina;
    private Double finansijskiPregled;
    private Date datumGenerisanja;

    // Konstruktori
    public IzvjestajDto() {}

    public IzvjestajDto(String tipIzvjestaja, Integer brojPacijenata, Integer brojTermina, Double finansijskiPregled) {
        this.tipIzvjestaja = tipIzvjestaja;
        this.brojPacijenata = brojPacijenata;
        this.brojTermina = brojTermina;
        this.finansijskiPregled = finansijskiPregled;
        this.datumGenerisanja = new Date();
    }

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipIzvjestaja() { return tipIzvjestaja; }
    public void setTipIzvjestaja(String tipIzvjestaja) { this.tipIzvjestaja = tipIzvjestaja; }

    public Integer getBrojPacijenata() { return brojPacijenata; }
    public void setBrojPacijenata(Integer brojPacijenata) { this.brojPacijenata = brojPacijenata; }

    public Integer getBrojTermina() { return brojTermina; }
    public void setBrojTermina(Integer brojTermina) { this.brojTermina = brojTermina; }

    public Double getFinancijskiPregled() { return finansijskiPregled; }
    public void setFinancijskiPregled(Double finansijskiPregled) { this.finansijskiPregled = finansijskiPregled; }

    public Date getDatumGenerisanja() { return datumGenerisanja; }
    public void setDatumGenerisanja(Date datumGenerisanja) { this.datumGenerisanja = datumGenerisanja; }
}