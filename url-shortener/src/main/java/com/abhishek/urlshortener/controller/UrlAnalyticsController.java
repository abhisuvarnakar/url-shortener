package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.ApiResponse;
import com.abhishek.urlshortener.dto.UrlAnalyticsResponseDTO;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.service.UrlAnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class UrlAnalyticsController {

    private final UrlAnalyticsService urlAnalyticsService;

    public UrlAnalyticsController(UrlAnalyticsService urlAnalyticsService) {
        this.urlAnalyticsService = urlAnalyticsService;
    }

    @GetMapping("/{urlId}")
    public ResponseEntity<?> getAnalytics(
            @PathVariable Long urlId,
            @RequestParam(defaultValue = "7") int days) {

        UrlAnalyticsResponseDTO responseDTO = urlAnalyticsService.getAnalytics(urlId, days);

        return ResponseEntity.ok(new ApiResponse<>(Status.SUCCESS, responseDTO, ""));
    }
}
