package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.entity.enums.Status;

public interface UrlStatsService {

    long getTotalUrlsCount();

    Long getTotalClickCount();

    long getActiveUrlsCount();

    Long getAverageClicksPerUrl();

    long getExpiringUrlsCountInNextDays(int days);

    UrlResponseDTO getTopPerformingUrl(Status status);
}
