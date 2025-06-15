package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.service.UrlAnalyticsService;
import com.abhishek.urlshortener.service.UrlService;
import com.abhishek.urlshortener.service.UrlStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;

@Controller
public class JspPageController {

    private final UrlStatsService urlStatsService;
    private final UrlService urlService;
    private final UrlAnalyticsService urlAnalyticsService;

    public JspPageController(UrlStatsService urlStatsService, UrlService urlService,
                             UrlAnalyticsService urlAnalyticsService) {
        this.urlStatsService = urlStatsService;
        this.urlService = urlService;
        this.urlAnalyticsService = urlAnalyticsService;
    }


    @GetMapping("/")
    public String rootPage() {
        return "redirect:/home";
    }

    @GetMapping("/url/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(
            @PathVariable String shortCode,
            HttpServletRequest request,
            HttpServletResponse response) {

        ShortenedUrl url = urlService.getOriginalUrl(shortCode);

        if (url == null || url.getStatus() == Status.EXPIRED) {
            return ResponseEntity.notFound().build();
        }

        urlService.logAnalytics(request, url);

        URI originalUri = URI.create(url.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.FOUND).location(originalUri).build();
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("totalUrls", 10);
        model.addAttribute("totalClicks", 15);
        model.addAttribute("totalUsers", 5);
        model.addAttribute("totalCountries", 2);

        return "home";
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "signup";
    }

    @GetMapping("/signin")
    public String signInPage() {
        return "signin";
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "logout";
    }

    @GetMapping("/profile")
    public String profilePage() {
        return "profile";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model,
                            @RequestParam(defaultValue = "1") int page) {

        model.addAttribute("totalUrls", urlStatsService.getTotalUrlsCount());
        model.addAttribute("totalClicks", urlStatsService.getTotalClickCount());
        model.addAttribute("activeUrls", urlStatsService.getActiveUrlsCount());
        model.addAttribute("avgClicksPerUrl", urlStatsService.getAverageClicksPerUrl());

        try {
            UrlResponseDTO urlResponseDTO = urlStatsService.getTopPerformingUrl(Status.ACTIVE);
            model.addAttribute("topPerformingUrl", Map.of("shortUrl", urlResponseDTO.getShortCode(),
                    "clicks", urlResponseDTO.getClickCount()));
        } catch (ResourceNotFoundException e) {
            model.addAttribute("topPerformingUrl", Map.of("shortUrl", "", "clicks", "0"));
        }

        model.addAttribute("clickRate24h", urlStatsService.getClickRate24h());
        model.addAttribute("clickChange24h", urlStatsService.getClickChange24h());
        model.addAttribute("topCountry", urlStatsService.getTopCountry());
        model.addAttribute("expiringSoonCount", urlStatsService.getExpiringUrlsCountInNextDays(7));

        return "dashboard";
    }

    @GetMapping("/url-analytics/{urlId}")
    public String showAnalyticsPage(@PathVariable Long urlId, Model model) {
        model.addAttribute("urlId", urlId);
        return "url-analytics";
    }

    @GetMapping("/dashboard/view-url")
    public String viewAllUrls(Model model,
                              @RequestParam(required = false) String searchTerm,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String fromDate,
                              @RequestParam(required = false) String toDate,
                              @RequestParam(required = false) Integer minClicks,
                              @RequestParam(required = false) Integer maxClicks,
                              @RequestParam(defaultValue = "createdAt") String sortField,
                              @RequestParam(defaultValue = "DESC") String sortOrder,
                              @RequestParam(defaultValue = "10") int urlsPerPage,
                              @RequestParam(defaultValue = "1") int page) {

        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("status", status);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("minClicks", minClicks);
        model.addAttribute("maxClicks", maxClicks);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        model.addAttribute("urlsPerPage", urlsPerPage);
        model.addAttribute("currentPage", page);

        return "view-url";
    }

}
