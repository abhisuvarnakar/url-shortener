package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.*;
import com.abhishek.urlshortener.entity.User;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.service.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private static final Logger log = LoggerFactory.getLogger(UrlController.class);
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
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

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletResponse response) throws IOException {
        String longUrl = urlService.getOriginalUrl(shortCode);
        response.sendRedirect(longUrl);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchUrls(@RequestBody UrlSearchCriteria searchCriteria,
                                        @RequestParam(defaultValue = "10") int urlsPerPage,
                                        @RequestParam(defaultValue = "1") int page) {

        try {
            int pageNumber = Math.max(0, page - 1);
            searchCriteria.setUserId(getAuthenticatedUser().getId());
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

            UrlResponseDTO urlResponseDTO = urlService.regenerateUrl(request.getUrlId());

            return ResponseEntity.ok(new ApiResponse<>(Status.SUCCESS, urlResponseDTO, "URL " +
                    "regenerated " +
                    "successfully"));
        } catch (Exception e) {
            log.error("Error regenerating URL: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(Status.FAILED, null,
                            "Failed to regenerate URL: " + e.getMessage()));
        }
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new ResourceNotFoundException("Authenticated user not found.");
        }

        return user;
    }

}
