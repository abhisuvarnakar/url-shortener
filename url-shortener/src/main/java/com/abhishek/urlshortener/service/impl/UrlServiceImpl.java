package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.*;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.UrlAnalytics;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.repository.UrlAnalyticsRepository;
import com.abhishek.urlshortener.repository.UrlRepository;
import com.abhishek.urlshortener.security.JwtService;
import com.abhishek.urlshortener.service.CountryService;
import com.abhishek.urlshortener.service.UrlService;
import com.abhishek.urlshortener.specification.UrlSpecification;
import com.abhishek.urlshortener.sql.UrlSql;
import com.abhishek.urlshortener.util.ShortCodeGenerator;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final CountryService countryService;
    private final UrlSql urlSql;

    public UrlServiceImpl(UrlRepository urlRepository,
                          UrlAnalyticsRepository urlAnalyticsRepository, ModelMapper modelMapper,
                          JwtService jwtService, CountryService countryService, UrlSql urlSql) {

        this.urlRepository = urlRepository;
        this.urlAnalyticsRepository = urlAnalyticsRepository;
        this.modelMapper = modelMapper;
        this.jwtService = jwtService;
        this.countryService = countryService;
        this.urlSql = urlSql;
    }

    @Override
    public UrlResponseDTO createShortUrl(UrlRequestDTO request) {
        User user = jwtService.getAuthenticatedUser();

        if (StringUtils.isBlank(request.getOriginalUrl())) {
            throw new RuntimeException("Original URL must not be blank.");
        }

        String customAlias = request.getCustomAlias();
        if (StringUtils.isNotBlank(customAlias) && urlRepository
                .findByCustomAlias(customAlias).isPresent()) {
            throw new IllegalArgumentException("Custom alias is not available. Choose another or "
                    + "use default.");
        }

        if (urlRepository.findByOriginalUrlAndUser_Id(request.getOriginalUrl(),
                jwtService.getAuthenticatedUser().getId()).isPresent()) {
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
    public ShortenedUrl getOriginalUrl(String shortCode) {
        ShortenedUrl url =
                urlRepository.findByShortCode(shortCode).orElseThrow(() -> new RuntimeException(
                        "Short URL not found"));

        if (url.getExpiresAt().before(new Date()) || url.getStatus() != Status.ACTIVE) {
            throw new RuntimeException("URL expired or disabled.");
        }

        url.setClickCount(url.getClickCount() + 1);
        urlRepository.save(url);

        return url;
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
    public UrlResponseDTO regenerateUrl(Long urlId, String customAlias) {
        ShortenedUrl url = urlRepository.findById(urlId)
                .orElseThrow(() -> new ResourceNotFoundException("URL not found with ID: " + urlId));

        String newShortCode;

        if (StringUtils.isNotBlank(customAlias) && urlRepository
                .findByCustomAlias(customAlias).isPresent()) {
            throw new IllegalArgumentException("Custom alias is not available. Choose another or "
                    + "use default.");
        } else if (StringUtils.isNotBlank(customAlias)) {
            newShortCode = customAlias.trim();
        } else {
            do {
                newShortCode = ShortCodeGenerator.generateCode(System.nanoTime());
            } while (urlRepository.existsByShortCode(newShortCode));
        }

        url.setShortCode(newShortCode);
        url.setCreatedAt(new Date());
        url.setExpiresAt(Date.from(url.getCreatedAt().toInstant().plus(Duration.ofDays(EXPIRE_DURATION))));
        url.setClickCount(0L);
        url.setStatus(Status.ACTIVE);
        urlSql.deleteAnalyticsByUrlId(url.getId());

        ShortenedUrl savedUrl = urlRepository.save(url);
        log.info("Successfully regenerated URL with ID: {}, New short code: {}", urlId,
                newShortCode);

        return modelMapper.map(savedUrl, UrlResponseDTO.class);
    }

    @Override
    public void logAnalytics(HttpServletRequest request, ShortenedUrl shortenedUrl) {
        String ipAddress = request.getRemoteAddr();
        String userAgentString = request.getHeader("User-Agent");
        String referrer = request.getHeader("Referer");

        UserAgentInfoDTO uaInfo = parseUserAgent(userAgentString);

        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setShortenedUrl(shortenedUrl);
        urlAnalytics.setClickTime(new Date());
        urlAnalytics.setIpAddress(ipAddress);
        urlAnalytics.setCountry(countryService.getCountryFromIp(ipAddress));
        urlAnalytics.setReferrer(referrer);
        urlAnalytics.setUserAgent(userAgentString);
        urlAnalytics.setDeviceType(uaInfo.getDeviceType());
        urlAnalytics.setBrowser(uaInfo.getBrowser());
        urlAnalytics.setOperatingSystem(uaInfo.getOperatingSystem());
        urlAnalytics.setPlatform(uaInfo.getPlatform());

        urlAnalyticsRepository.save(urlAnalytics);
    }

    @Override
    public void extendExpiry(Long urlId, int extensionDays) {
        urlSql.extendExpiry(urlId, extensionDays);
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

    private UserAgentInfoDTO parseUserAgent(String userAgentStr) {
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();

        return new UserAgentInfoDTO(operatingSystem.getDeviceType().getName(), browser.getName(),
                operatingSystem.getName(), operatingSystem.getGroup().getName());
    }
}
