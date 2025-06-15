package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.CountryDTO;
import com.abhishek.urlshortener.service.CountryService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    private static final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);
    private final RestTemplate restTemplate;

    private final List<CountryDTO> countryDTOList;

    public CountryServiceImpl(RestTemplate restTemplate,
                              @Qualifier("countryListBean") List<CountryDTO> countryDTOList) {
        this.restTemplate = restTemplate;
        this.countryDTOList = countryDTOList;
    }

    @Override
    public List<CountryDTO> getAllCountries() {
        return countryDTOList;
    }

    @Override
    public String getCountryName(String cca3Code) {

        return getAllCountries().stream()
                .filter(c -> c.getCode().equalsIgnoreCase(cca3Code))
                .map(CountryDTO::getName)
                .findFirst()
                .orElse("Unknown");
    }

    @Override
    public String getCountryFromIp(String ip) {
        try {
            String url = "https://ipwho.is/" + ip;
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            JsonNode body = response.getBody();

            if (body != null && body.path("success").asBoolean()) {
                String cca3 = body.path("country_code").asText();
                return getCountryName(cca3).toUpperCase();
            }
            //for development
            String reason = body != null ? body.path("message").asText() : "Unknown error";
            if ("Reserved range".equalsIgnoreCase(reason)) {
                return "INDIA";
            }
        } catch (Exception ignored) {

        }
        return "Unknown";
    }
}
