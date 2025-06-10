package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.*;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.repository.UrlRepository;
import com.abhishek.urlshortener.service.UrlService;
import com.abhishek.urlshortener.specification.UrlSpecification;
import com.abhishek.urlshortener.util.ShortCodeGenerator;
import com.abhishek.urlshortener.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UrlServiceImpl implements UrlService {

    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);
    private static final int EXPIRE_DURATION = 7;
    private final UrlRepository urlRepository;
    private final ModelMapper modelMapper;

    public UrlServiceImpl(UrlRepository urlRepository, ModelMapper modelMapper) {
        this.urlRepository = urlRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UrlResponseDTO createShortUrl(UrlRequestDTO request) {
        User user = getAuthenticatedUser();

        if (StringUtils.isBlank(request.getOriginalUrl())) {
            throw new RuntimeException("Original URL must not be blank.");
        }

        String customAlias = request.getCustomAlias();
        if (StringUtils.isNotBlank(customAlias) && urlRepository
                .findByCustomAlias(customAlias).isPresent()) {
            throw new IllegalArgumentException("Custom alias already exists. Choose another or " + "use default.");
        }

        if (urlRepository.findByOriginalUrlAndUser_Id(request.getOriginalUrl(),
                getAuthenticatedUser().getId()).isPresent()) {
            throw new IllegalArgumentException("This URL is already shortened.");
        }

        ShortenedUrl url = new ShortenedUrl();
        url.setOriginalUrl(request.getOriginalUrl());
        url.setUser(user);
        url.setClickCount(0L);
        url.setCreatedAt(new Date());
        url.setExpiresAt(Date.from(url.getCreatedAt().toInstant().plus(Duration.ofDays(EXPIRE_DURATION))));
        url.setStatus(Status.ACTIVE);

        if (customAlias != null) {
            url.setCustomAlias(customAlias);
            url.setShortCode(customAlias);
            url = urlRepository.save(url);

            return modelMapper.map(url, UrlResponseDTO.class);
        }

        urlRepository.save(url);

        String shortCode = ShortCodeGenerator.generateCode(System.nanoTime());
        url.setShortCode(shortCode);

        url = urlRepository.save(url);

        return modelMapper.map(url, UrlResponseDTO.class);
    }

    @Override
    public String getOriginalUrl(String shortCode) {
        ShortenedUrl url =
                urlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException(
                        "Short URL not found"));

        if (url.getExpiresAt().before(new Date()) || url.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("URL expired or disabled.");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url.getOriginalUrl();
    }

    @Override
    public UrlPaginationResponseDTO getExpiringUrls(int days, int page) {
        if (!(days == 7 || days == 30 || days == 90)) {
            throw new IllegalArgumentException("Invalid days value: must be 7, 30, or 90.");
        }

        User user = getAuthenticatedUser();

        Date now = new Date();
        Date cutoff = Date.from(now.toInstant().plus(Duration.ofDays(days)));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("expires_at").ascending());
        Page<ShortenedUrl> urlPage = urlRepository.findExpiringUrl(user.getId(), now, cutoff,
                pageable);

        List<ExpiringUrlDTO> dtoList = urlPage.getContent().stream().map(url -> {
            long daysLeft =
                    Duration.between(now.toInstant(), url.getExpiresAt().toInstant()).toDays();

            ExpiringUrlDTO expiringUrlDTO = modelMapper.map(url, ExpiringUrlDTO.class);
            expiringUrlDTO.setDaysLeft(daysLeft);

            return expiringUrlDTO;
        }).toList();

        return new UrlPaginationResponseDTO(dtoList, urlPage);
    }

    @Override
    public Page<UrlSearchResponseDTO> searchUrls(UrlSearchCriteria criteria, int page, int size) {
        Specification<ShortenedUrl> specification = UrlSpecification.build(criteria);

        Sort sort = createSort(criteria.getSortField(), criteria.getSortOrder());
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ShortenedUrl> urlPage = urlRepository.findAll(specification, pageable);
        return urlPage.map(url -> modelMapper.map(url, UrlSearchResponseDTO.class));
    }

    @Override
    public boolean deleteUrl(Long urlId) {
        Optional<ShortenedUrl> urlOptional = urlRepository.findById(urlId);
        if (urlOptional.isEmpty()) {
            throw new ResourceNotFoundException("URL not found with ID: " + urlId);
        }
        urlRepository.deleteById(urlId);
        log.info("Successfully deleted URL with ID: {}", urlId);
        return true;
    }

    @Override
    public UrlResponseDTO regenerateUrl(Long urlId) {
        ShortenedUrl url = urlRepository.findById(urlId)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found with ID: " + urlId));

        String newShortCode;
        do {
            newShortCode = ShortCodeGenerator.generateCode(System.nanoTime());
        } while (urlRepository.existsByShortCode(newShortCode));

        url.setShortCode(newShortCode);
        url.setCreatedAt(new Date());
        url.setExpiresAt(Date.from(url.getCreatedAt().toInstant().plus(Duration.ofDays(EXPIRE_DURATION))));
        url.setStatus(Status.ACTIVE);

        ShortenedUrl savedUrl = urlRepository.save(url);
        log.info("Successfully regenerated URL with ID: {}, New short code: {}", urlId,
                newShortCode);

        return modelMapper.map(savedUrl, UrlResponseDTO.class);
    }

    private Sort createSort(String sortField, String sortOrder) {
        if (StringUtils.isBlank(sortField) || !List.of("createdAt", "clickCount", "status").contains(sortField)) {
            sortField = "createdAt";
        }
        if (StringUtils.isBlank(sortOrder)) {
            sortOrder = "DESC";
        }

        Sort.Direction direction = "ASC".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC :
                Sort.Direction.DESC;

        return Sort.by(direction, sortField);
    }

    private UrlSearchResponseDTO convertToDto(ShortenedUrl url) {
        UrlSearchResponseDTO dto = new UrlSearchResponseDTO();
        dto.setId(url.getId());
        dto.setOriginalUrl(url.getOriginalUrl());
        dto.setShortCode(url.getShortCode());
        dto.setStatus(url.getStatus().toString());
        dto.setClickCount(url.getClickCount());
        dto.setUserId(url.getUser().getId().toString());

        String displayFormatter = "MMM dd, yyyy HH:mm:ss";
        if (url.getCreatedAt() != null) {
            dto.setCreatedAt(Utils.formatDate(url.getCreatedAt(), displayFormatter));
        }

        if (url.getExpiresAt() != null) {
            dto.setExpiresAt(Utils.formatDate(url.getExpiresAt(), displayFormatter));
        }

        return dto;
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        return user;
    }
}
