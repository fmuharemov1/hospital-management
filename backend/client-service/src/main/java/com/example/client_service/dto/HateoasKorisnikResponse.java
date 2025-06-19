package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class HateoasKorisnikResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    public static class Embedded {
        @JsonProperty("korisnikList")  // ← PROMENITE SA "korisnici" NA "korisnikList"
        private List<KorisnikDto> korisnikList;

        public List<KorisnikDto> getKorisnikList() {
            return korisnikList;
        }

        public void setKorisnikList(List<KorisnikDto> korisnikList) {
            this.korisnikList = korisnikList;
        }
    }

    public Embedded getEmbedded() {
        return embedded;
    }

    public void setEmbedded(Embedded embedded) {
        this.embedded = embedded;
    }

    public List<KorisnikDto> getKorisnici() {
        return embedded != null ? embedded.getKorisnikList() : java.util.Collections.emptyList();
    }
}