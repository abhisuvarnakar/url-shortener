package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.entity.enums.Status;

import java.util.Map;

public interface UrlStatsService {

    long getTotalUrlsCount();

    Long getTotalClickCount();

    long getActiveUrlsCount();

    Long getAverageClicksPerUrl();

    long getExpiringUrlsCountInNextDays(int days);

    UrlResponseDTO getTopPerformingUrl(Status status);

    long getClickRate24h();

    long getClickChange24h();

    Map<String, Object> getTopCountry();
}
