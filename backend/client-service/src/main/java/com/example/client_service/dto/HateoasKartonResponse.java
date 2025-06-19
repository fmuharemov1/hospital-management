package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

// U com.example.client_service.dto paketu
public class HateoasKartonResponse {
    @JsonProperty("_embedded")
    private EmbeddedKartoni embedded;

    public List<KartonEntryDto> getKartoni() {
        return embedded != null ? embedded.getKartonList() : new ArrayList<>();
    }

    // Getters/setters
    public EmbeddedKartoni getEmbedded() { return embedded; }
    public void setEmbedded(EmbeddedKartoni embedded) { this.embedded = embedded; }

    public static class EmbeddedKartoni {
        @JsonProperty("kartonList")  // ili možda "kartonEntryList" - zavisi od EMR Service-a
        private List<KartonEntryDto> kartonList;

        public List<KartonEntryDto> getKartonList() { return kartonList; }
        public void setKartonList(List<KartonEntryDto> kartonList) { this.kartonList = kartonList; }
    }
}
