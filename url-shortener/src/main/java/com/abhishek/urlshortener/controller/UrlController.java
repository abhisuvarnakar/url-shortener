package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.*;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.security.JwtService;
import com.abhishek.urlshortener.service.UrlService;
import com.abhishek.urlshortener.sql.UrlSql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private static final Logger log = LoggerFactory.getLogger(UrlController.class);
    private final UrlService urlService;
    private final JwtService jwtService;
    private final UrlSql urlSql;

    public UrlController(UrlService urlService, JwtService jwtService, UrlSql urlSql) {
        this.urlService = urlService;
        this.jwtService = jwtService;
        this.urlSql = urlSql;
    }

    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody UrlRequestDTO urlRequestDTO) {
        try {
            UrlResponseDTO urlResponseDTO = urlService.createShortUrl(urlRequestDTO);
            ApiResponse<UrlResponseDTO> response = new ApiResponse<>(Status.SUCCESS,
                    urlResponseDTO, "URL shortened successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>(Status.FAILED, null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchUrls(@RequestBody UrlSearchCriteria searchCriteria,
                                        @RequestParam(defaultValue = "10") int urlsPerPage,
                                        @RequestParam(defaultValue = "1") int page) {

        try {
            int pageNumber = Math.max(0, page - 1);
            searchCriteria.setUserId(jwtService.getAuthenticatedUser().getId());
            Page<UrlSearchResponseDTO> urls = urlService.searchUrls(searchCriteria, pageNumber,
                    urlsPerPage);

            ApiResponse<Page<UrlSearchResponseDTO>> response = new ApiResponse<>(Status.SUCCESS,
                    urls,
                    "");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>(Status.FAILED, null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @DeleteMapping("/deleteUrl")
    public ResponseEntity<?> deleteUrl(@RequestBody UrlRequestDTO request) {
        try {
            if (request.getUrlId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(Status.FAILED, null,
                        "URL ID is required"));
            }
            log.info("Received delete request for URL ID: {}", request.getUrlId());

            boolean deleted = urlService.deleteUrl(request.getUrlId());

            if (deleted) {
                return ResponseEntity.ok(new ApiResponse<>(Status.SUCCESS, null, "URL deleted " +
                        "successfully"));
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(Status.FAILED, null,
                            "Failed to delete URL: " + e.getMessage()));
        }
    }

    @PostMapping("/regenerateUrl")
    public ResponseEntity<?> regenerateUrl(@RequestBody UrlRequestDTO request) {
        try {
            if (request.getUrlId() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(Status.FAILED, null,
                        "URL ID is required"));
            }
            log.info("Received regenerate request for URL ID: {}", request.getUrlId());

            UrlResponseDTO urlResponseDTO = urlService.regenerateUrl(request.getUrlId(),
                    request.getCustomAlias());

            return ResponseEntity.ok(new ApiResponse<>(Status.SUCCESS, urlResponseDTO, "URL " +
                    "regenerated " +
                    "successfully"));
        } catch (IllegalArgumentException e) {
            ApiResponse<Object> errorResponse = new ApiResponse<>(Status.FAILED, null,
                    e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            log.error("Error regenerating URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(Status.FAILED, null,
                            "Failed to regenerate URL: " + e.getMessage()));
        }
    }

    @PostMapping("/extendExpiry")
    public ResponseEntity<ApiResponse<Void>> extendExpiry(@RequestBody UrlRequestDTO request) {
        Long urlId = request.getUrlId();
        Integer extensionDays = request.getExtensionDays();

        if (urlId == null || extensionDays == null || extensionDays <= 0) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(Status.FAILED, null, "Valid URL ID and positive extension " +
                            "days are required")
            );
        }

        log.info("Extending expiry for URL ID {} by {} days", urlId, extensionDays);

        urlService.extendExpiry(urlId, extensionDays);

        return ResponseEntity.ok(
                new ApiResponse<>(Status.SUCCESS, null, "URL expiration extended successfully")
        );
    }

    @Scheduled(fixedRate = 60000)
    public void expireLinks() {
        urlSql.expireOldUrls(new Date());
    }
}
