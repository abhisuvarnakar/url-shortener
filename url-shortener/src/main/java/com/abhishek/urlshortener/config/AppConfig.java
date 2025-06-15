package com.abhishek.urlshortener.config;

import com.abhishek.urlshortener.dto.CountryDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Value("${countries.api.url}")
    private String countriesApiUrl;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("userCache");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean(name = "countryListBean")
    public List<CountryDTO> getAllCountries() {
        ResponseEntity<JsonNode> response = restTemplate().getForEntity(countriesApiUrl,
                JsonNode.class);
        JsonNode body = response.getBody();

        List<CountryDTO> list = new ArrayList<>();

        if (body != null && body.isArray()) {
            for (JsonNode node : body) {
                String name = node.path("name").path("common").asText();
                String code = node.path("cca3").asText();
                list.add(new CountryDTO(code, name));
            }
        }

        log.info("Retrieved {} country data from API.", list.size());
        return list;
    }
}
