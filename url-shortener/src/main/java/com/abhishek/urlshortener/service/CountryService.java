package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.CountryDTO;

import java.util.List;

public interface CountryService {

    List<CountryDTO> getAllCountries();

    String getCountryName(String cca3Code);

    String getCountryFromIp(String ip);

}
