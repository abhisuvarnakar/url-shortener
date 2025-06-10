package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.repository.UrlRepository;
import com.abhishek.urlshortener.service.UrlStatsService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UrlStatsServiceImpl implements UrlStatsService {

    private final UrlRepository urlRepository;
    private final ModelMapper modelMapper;

    public UrlStatsServiceImpl(UrlRepository urlRepository, ModelMapper modelMapper) {
        this.urlRepository = urlRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public long getTotalUrlsCount() {
        return urlRepository.countTotalUrls(getAuthenticatedUser().getId());
    }

    @Override
    public Long getTotalClickCount() {
        return urlRepository.getTotalClickCount(getAuthenticatedUser().getId());
    }

    @Override
    public long getActiveUrlsCount() {
        return urlRepository.countByStatus(Status.ACTIVE, getAuthenticatedUser().getId());
    }

    @Override
    public Long getAverageClicksPerUrl() {
        return urlRepository.getAverageClickPerUrl(getAuthenticatedUser().getId()).longValue();
    }

    @Override
    public long getExpiringUrlsCountInNextDays(int days) {
        LocalDate now = LocalDate.now();
        LocalDate futureDate = now.plusDays(days);

        return urlRepository.countExpiringUrlsInNextDays(java.sql.Date.valueOf(now),
                java.sql.Date.valueOf(futureDate), Status.ACTIVE, getAuthenticatedUser().getId());
    }

    @Override
    public UrlResponseDTO getTopPerformingUrl(Status status) {
        Optional<ShortenedUrl> optionalShortenedUrl =
                urlRepository.findTopPerformingUrl(status.name(), getAuthenticatedUser().getId());
        if (optionalShortenedUrl.isEmpty()) {
            throw new ResourceNotFoundException("No URL data present.");
        }
        ShortenedUrl shortenedUrl = optionalShortenedUrl.get();

        return modelMapper.map(shortenedUrl, UrlResponseDTO.class);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        return user;
    }

}
