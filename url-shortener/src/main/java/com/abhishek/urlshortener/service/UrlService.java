package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.UrlRequestDTO;
import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.dto.UrlSearchCriteria;
import com.abhishek.urlshortener.dto.UrlSearchResponseDTO;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;

public interface UrlService {

    UrlResponseDTO createShortUrl(UrlRequestDTO request);

    ShortenedUrl getOriginalUrl(String shortCode);

    Page<UrlSearchResponseDTO> searchUrls(UrlSearchCriteria criteria, int page, int size);

    boolean deleteUrl(Long urlId);

    UrlResponseDTO regenerateUrl(Long urlId, String customAlias);

    void logAnalytics(HttpServletRequest request, ShortenedUrl shortenedUrl);

    void extendExpiry(Long urlId, int extensionDays);

}
