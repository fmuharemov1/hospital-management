
package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class KartonResponse {
    @JsonProperty("_embedded")
    private Embedded embedded;

    public Embedded getEmbedded() { return embedded; }
    public void setEmbedded(Embedded embedded) { this.embedded = embedded; }

    public static class Embedded {
        @JsonProperty("kartonList")  // Možda treba biti "medicalKartonList" - provjeri JSON
        private List<KartonEntryDto> kartonList;

        public List<KartonEntryDto> getKartonList() { return kartonList; }
        public void setKartonList(List<KartonEntryDto> kartonList) { this.kartonList = kartonList; }
    }
}