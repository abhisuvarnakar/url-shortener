package com.abhishek.urlshortener.service;

import com.abhishek.urlshortener.dto.*;
import org.springframework.data.domain.Page;

public interface UrlService {

    UrlResponseDTO createShortUrl(UrlRequestDTO request);

    String getOriginalUrl(String shortCode);

    UrlPaginationResponseDTO getExpiringUrls(int days, int page);

    Page<UrlSearchResponseDTO> searchUrls(UrlSearchCriteria criteria, int page, int size);

    boolean deleteUrl(Long urlId);

    UrlResponseDTO regenerateUrl(Long urlId);
}
