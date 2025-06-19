package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

// HateoasDijagnozaResponse.java
public class HateoasDijagnozaResponse {
    @JsonProperty("_embedded")
    private EmbeddedDijagnoze embedded;

    public List<DijagnozaDto> getDijagnoze() {
        return embedded != null ? embedded.getDijagnozaList() : new ArrayList<>();
    }

    public static class EmbeddedDijagnoze {
        @JsonProperty("dijagnozaList") // ili kako god EMR Service imenuje
        private List<DijagnozaDto> dijagnozaList;

        public List<DijagnozaDto> getDijagnozaList() { return dijagnozaList; }
        public void setDijagnozaList(List<DijagnozaDto> dijagnozaList) { this.dijagnozaList = dijagnozaList; }
    }
}