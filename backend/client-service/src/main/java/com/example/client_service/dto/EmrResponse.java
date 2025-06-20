package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class EmrResponse {
    @JsonProperty("_embedded")
    private Embedded embedded;

    public Embedded getEmbedded() { return embedded; }
    public void setEmbedded(Embedded embedded) { this.embedded = embedded; }

    public static class Embedded {
        @JsonProperty("korisnikList")
        private List<KorisnikDto> korisnikList;

        public List<KorisnikDto> getKorisnikList() { return korisnikList; }
        public void setKorisnikList(List<KorisnikDto> korisnikList) { this.korisnikList = korisnikList; }
    }
}