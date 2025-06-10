package com.abhishek.urlshortener.controller;

import com.abhishek.urlshortener.dto.UrlResponseDTO;
import com.abhishek.urlshortener.entity.enums.Status;
import com.abhishek.urlshortener.exception.ResourceNotFoundException;
import com.abhishek.urlshortener.service.UrlStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class JspPageController {

    private final UrlStatsService urlService;

    public JspPageController(UrlStatsService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/")
    public String rootPage() {
        return "redirect:/home";
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
                            @RequestParam(defaultValue = "1") int page,
                            HttpSession session) {

        model.addAttribute("totalUrls", urlService.getTotalUrlsCount());
        model.addAttribute("totalClicks", urlService.getTotalClickCount());
        model.addAttribute("activeUrls", urlService.getActiveUrlsCount());
        model.addAttribute("avgClicksPerUrl", urlService.getAverageClicksPerUrl());

        try {
            UrlResponseDTO urlResponseDTO = urlService.getTopPerformingUrl(Status.ACTIVE);
            model.addAttribute("topPerformingUrl", Map.of("shortUrl", urlResponseDTO.getShortCode(),
                    "clicks", urlResponseDTO.getClickCount()));
        } catch (ResourceNotFoundException e) {
            model.addAttribute("topPerformingUrl", Map.of("shortUrl", "TBD", "clicks", "52"));
        }

        model.addAttribute("clickRate24h", 23);
        model.addAttribute("clickChange24h", 45);
        model.addAttribute("topCountry", Map.of("name", "INDIA", "percentage", "95"));
        model.addAttribute("expiringSoonCount", urlService.getExpiringUrlsCountInNextDays(8));

        return "dashboard";
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
