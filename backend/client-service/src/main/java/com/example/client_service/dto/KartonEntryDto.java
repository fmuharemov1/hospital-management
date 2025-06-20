package com.example.client_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class KartonEntryDto {
    private Long id;
    private String brojKartona;
    private LocalDateTime datumKreiranja;
    private Long pacijentUuid;

    // Lista dijagnoza za ovaj karton
    private List<DijagnozaDto> dijagnoze;

    // Getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrojKartona() { return brojKartona; }
    public void setBrojKartona(String brojKartona) { this.brojKartona = brojKartona; }

    public LocalDateTime getDatumKreiranja() { return datumKreiranja; }
    public void setDatumKreiranja(LocalDateTime datumKreiranja) { this.datumKreiranja = datumKreiranja; }

    public Long getPacijentUuid() { return pacijentUuid; }
    public void setPacijentUuid(Long pacijentUuid) { this.pacijentUuid = pacijentUuid; }

    public List<DijagnozaDto> getDijagnoze() { return dijagnoze; }
    public void setDijagnoze(List<DijagnozaDto> dijagnoze) { this.dijagnoze = dijagnoze; }

    public Long getPatientId() {
        return pacijentUuid;
    }

    public void setPatientId(Long patientId) {
        this.pacijentUuid = patientId;
    }
}
