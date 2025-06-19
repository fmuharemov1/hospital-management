package com.example.client_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class HateoasTerapijaResponse {
    @JsonProperty("_embedded")
    private EmbeddedTerapije embedded;

    public List<TerapijaDto> getTerapije() {
        return embedded != null ? embedded.getTerapijaList() : new ArrayList<>();
    }

    public static class EmbeddedTerapije {
        @JsonProperty("terapijaList")
        private List<TerapijaDto> terapijaList;

        public List<TerapijaDto> getTerapijaList() { return terapijaList; }
        public void setTerapijaList(List<TerapijaDto> terapijaList) { this.terapijaList = terapijaList; }
    }
}
