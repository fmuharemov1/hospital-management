package com.example.client_service.service;


import com.example.client_service.dto.IzvjestajDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class IzvjestajService {

    private final RestTemplate restTemplate;

    @Value("${hospital.service.url:http://localhost:8085}")
    private String hospitalServiceUrl;

    public IzvjestajService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Page<IzvjestajDto> getAllIzvjestaji(int page, int size) {
        String url = UriComponentsBuilder
                .fromHttpUrl(hospitalServiceUrl + "/izvjestaji")
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString();

        // Za Page response, možete koristiti Map ili kreirati PageResponse wrapper
        ResponseEntity<List<IzvjestajDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IzvjestajDto>>() {}
        );

        return new PageImpl<>(response.getBody());
    }

    public List<IzvjestajDto> filterIzvjestaji(String tipIzvjestaja,
                                               Integer minBrojPacijenata,
                                               Double minIznos) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(hospitalServiceUrl + "/izvjestaji/filter")
                .queryParam("tipIzvjestaja", tipIzvjestaja);

        if (minBrojPacijenata != null) {
            builder.queryParam("minBrojPacijenata", minBrojPacijenata);
        }
        if (minIznos != null) {
            builder.queryParam("minIznos", minIznos);
        }

        ResponseEntity<List<IzvjestajDto>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<IzvjestajDto>>() {}
        );

        return response.getBody();
    }

    public IzvjestajDto createIzvjestaj(IzvjestajDto izvjestaj) {
        return restTemplate.postForObject(
                hospitalServiceUrl + "/izvjestaji",
                izvjestaj,
                IzvjestajDto.class
        );
    }

    public IzvjestajDto getIzvjestajById(Long id) {
        return restTemplate.getForObject(
                hospitalServiceUrl + "/izvjestaji/" + id,
                IzvjestajDto.class
        );
    }
}
