package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.repository.UrlRepository;
import com.abhishek.urlshortener.security.JwtService;
import com.abhishek.urlshortener.service.UrlStatsService;
import com.abhishek.urlshortener.sql.UrlSql;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class UrlStatsServiceImpl implements UrlStatsService {

    private final UrlRepository urlRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final UrlSql urlSql;

    public UrlStatsServiceImpl(UrlRepository urlRepository, ModelMapper modelMapper,
                               JwtService jwtService, UrlSql urlSql) {
        this.urlRepository = urlRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.urlSql = urlSql;
    }

    @Override
    public long getTotalUrlsCount() {
        return urlRepository.countTotalUrls(jwtService.getAuthenticatedUser().getId());
    }

    @Override
    public Long getTotalClickCount() {
        return urlRepository.getTotalClickCount(jwtService.getAuthenticatedUser().getId());
    }

    @Override
    public long getActiveUrlsCount() {
        return urlRepository.countByStatus(Status.ACTIVE,
                jwtService.getAuthenticatedUser().getId());
    }

    @Override
    public Long getAverageClicksPerUrl() {
        Double averageClick =
                urlRepository.getAverageClickPerUrl(jwtService.getAuthenticatedUser().getId());
        return Optional.ofNullable(averageClick)
                .map(Double::longValue)
                .orElse(0L);
    }

    @Override
    public long getExpiringUrlsCountInNextDays(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);

        return urlRepository.countExpiringUrlsInNextDays(java.sql.Date.valueOf(now),
                java.sql.Date.valueOf(futureDate), Status.ACTIVE,
                jwtService.getAuthenticatedUser().getId());
    }

    @Override
    public UrlResponseDTO getTopPerformingUrl(Status status) {
        Optional<ShortenedUrl> optionalShortenedUrl =
                urlRepository.findTopPerformingUrl(status.name(),
                        jwtService.getAuthenticatedUser().getId());
        if (optionalShortenedUrl.isEmpty()) {
            throw new ResourceNotFoundException("No URL data present.");
        }
        ShortenedUrl shortenedUrl = optionalShortenedUrl.get();

        return modelMapper.map(shortenedUrl, UrlResponseDTO.class);
    }

    @Override
    public long getClickRate24h() {
        return urlRepository.getClickRate24h(jwtService.getAuthenticatedUser().getId());
    }

    @Override
    public long getClickChange24h() {
        return urlSql.getClickChange24h(jwtService.getAuthenticatedUser().getId());
    }

    @Override
    public Map<String, Object> getTopCountry() {
        return urlSql.getTopCountry(jwtService.getAuthenticatedUser().getId());
    }

}
