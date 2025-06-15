package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UrlAnalyticsResponseDTO;

public interface UrlAnalyticsService {

    UrlAnalyticsResponseDTO getAnalytics(Long urlId, int days);
}
