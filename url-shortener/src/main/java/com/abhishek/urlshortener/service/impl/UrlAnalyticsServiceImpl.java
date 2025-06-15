package com.abhishek.urlshortener.service.impl;

import com.abhishek.urlshortener.dto.UrlAnalyticsResponseDTO;
import com.abhishek.urlshortener.dto.analytics.BrowserData;
import com.abhishek.urlshortener.dto.analytics.CountryData;
import com.abhishek.urlshortener.dto.analytics.DeviceData;
import com.abhishek.urlshortener.entity.ShortenedUrl;
import com.abhishek.urlshortener.repository.UrlAnalyticsRepository;
import com.abhishek.urlshortener.repository.UrlRepository;
import com.abhishek.urlshortener.service.UrlAnalyticsService;
import com.abhishek.urlshortener.sql.UrlSql;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UrlAnalyticsServiceImpl implements UrlAnalyticsService {

    private static final DateTimeFormatter LABEL_FORMAT = DateTimeFormatter.ofPattern("MMM d");
    private static final DateTimeFormatter KEY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final UrlRepository urlRepository;
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final UrlSql urlSql;

    public UrlAnalyticsServiceImpl(UrlRepository urlRepository,
                                   UrlAnalyticsRepository urlAnalyticsRepository, UrlSql urlSql) {
        this.urlRepository = urlRepository;
        this.urlAnalyticsRepository = urlAnalyticsRepository;
        this.urlSql = urlSql;
    }

    @Override
    public UrlAnalyticsResponseDTO getAnalytics(Long urlId, int days) {
        UrlAnalyticsResponseDTO response = new UrlAnalyticsResponseDTO();
        ShortenedUrl urlInfo = urlRepository.findById(urlId).orElseThrow();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);
        java.sql.Date startSqlDate = java.sql.Date.valueOf(startDate);

        Map<String, Long> stats = urlSql.getClickStats(urlId);
        Map<String, Long> clicksByDate = urlSql.getDailyClicks(urlId, startSqlDate)
                .stream()
                .collect(Collectors.toMap(
                        entry -> {
                            java.sql.Date sqlDate = (java.sql.Date) entry.get("date");
                            LocalDate localDate = sqlDate.toLocalDate();
                            return KEY_FORMAT.format(localDate);
                        },
                        entry -> ((Number) entry.get("clicks")).longValue()
                ));

        List<String> chartLabels = new ArrayList<>();
        List<Long> chartData = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            chartLabels.add(LABEL_FORMAT.format(date));
            chartData.add(clicksByDate.getOrDefault(KEY_FORMAT.format(date), 0L));
        }

        response.setShortUrl(urlInfo.getShortCode());
        response.setOriginalUrl(urlInfo.getOriginalUrl());
        response.setTotalClicks(stats.get("totalClicks"));
        response.setTodayClicks(stats.get("todayClicks"));
        response.setTodayChange(calculateDailyChange(urlId));
        response.setWeekClicks(stats.get("weekClicks"));
        response.setWeekChange(calculateWeeklyChange(urlId));
        response.setMonthClicks(stats.get("monthClicks"));
        response.setMonthChange(calculateMonthlyChange(urlId));
        response.setChartLabels(chartLabels);
        response.setChartData(chartData);
        response.setTopCountries(getCountryData(urlId, startSqlDate, stats.get("totalClicks")));
        response.setDeviceTypes(getDeviceData(urlId, startSqlDate, stats.get("totalClicks")));
        response.setBrowsers(getBrowserData(urlId, startSqlDate, stats.get("totalClicks")));

        return response;
    }

    private List<CountryData> getCountryData(Long urlId, java.sql.Date startDate,
                                             long totalClicks) {
        return urlSql.getTopCountries(urlId, startDate)
                .stream()
                .map(map -> new CountryData(
                        (String) map.get("name"),
                        (String) map.get("name"),
                        (Long) map.get("clicks"),
                        calculatePercentage((Long) map.get("clicks"), totalClicks)
                ))
                .toList();
    }

    private List<DeviceData> getDeviceData(Long urlId, java.sql.Date startDate, long totalClicks) {
        return urlSql.getDeviceTypes(urlId, startDate)
                .stream()
                .map(map -> {
                    String type = (String) map.get("type");
                    Long clicks = (Long) map.get("clicks");
                    return new DeviceData(type, getDeviceIcon(type), getDeviceColor(type), clicks
                            , calculatePercentage(clicks, totalClicks));
                })
                .toList();
    }

    private List<BrowserData> getBrowserData(Long urlId, java.sql.Date startDate,
                                             long totalClicks) {
        return urlSql.getBrowsers(urlId, startDate)
                .stream()
                .map(map -> {
                    String name = (String) map.get("name");
                    Long clicks = (Long) map.get("clicks");
                    return new BrowserData(name, getBrowserIcon(name), clicks,
                            calculatePercentage(clicks, totalClicks));
                })
                .toList();
    }

    private double calculatePercentage(long part, long total) {
        return total == 0 ? 0.0 : Math.round((part * 100.0 / total) * 10) / 10.0;
    }

    private Double calculateDailyChange(Long urlId) {
        LocalDate today = LocalDate.now();
        long todayClicks = urlAnalyticsRepository.countByUrlIdAndDate(urlId,
                java.sql.Date.valueOf(today));
        long yesterdayClicks = urlAnalyticsRepository.countByUrlIdAndDate(urlId,
                java.sql.Date.valueOf(today.minusDays(1)));
        return calculateChange(todayClicks, yesterdayClicks);
    }

    private Double calculateWeeklyChange(Long urlId) {
        LocalDate now = LocalDate.now();
        int thisWeek = now.get(WeekFields.ISO.weekOfYear());
        int lastWeek = now.minusWeeks(1).get(WeekFields.ISO.weekOfYear());
        long thisWeekClicks = urlAnalyticsRepository.countByUrlIdAndWeek(urlId, now.getYear(),
                thisWeek);
        long lastWeekClicks = urlAnalyticsRepository.countByUrlIdAndWeek(urlId,
                now.minusWeeks(1).getYear(), lastWeek);
        return calculateChange(thisWeekClicks, lastWeekClicks);
    }

    private Double calculateMonthlyChange(Long urlId) {
        LocalDate now = LocalDate.now();
        int thisMonth = now.getMonthValue();
        int lastMonth = now.minusMonths(1).getMonthValue();
        long thisMonthClicks = urlAnalyticsRepository.countByUrlIdAndMonth(urlId, now.getYear(),
                thisMonth);
        long lastMonthClicks = urlAnalyticsRepository.countByUrlIdAndMonth(urlId,
                now.minusMonths(1).getYear(), lastMonth);
        return calculateChange(thisMonthClicks, lastMonthClicks);
    }

    private Double calculateChange(long current, long previous) {
        if (previous == 0) return 0.0;
        return ((current - previous) * 100.0) / previous;
    }

    private String getDeviceIcon(String type) {
        String lower = type.toLowerCase();

        if (lower.contains("desktop")) return "fa-desktop";
        else if (lower.contains("mobile")) return "fa-mobile-alt";
        else if (lower.contains("tablet")) return "fa-tablet-alt";
        else return "fa-laptop";
    }

    private String getDeviceColor(String type) {
        String lower = type.toLowerCase();

        if (lower.contains("desktop")) return "#20c997";
        else if (lower.contains("mobile")) return "#6610f2";
        else if (lower.contains("tablet")) return "#fd7e14";
        else return "#6c757d";
    }


    private String getBrowserIcon(String name) {
        String lower = name.toLowerCase();

        if (lower.contains("chrome")) return "fa-chrome";
        else if (lower.contains("safari")) return "fa-safari";
        else if (lower.contains("firefox")) return "fa-firefox";
        else if (lower.contains("edge")) return "fa-edge";
        else if (lower.contains("opera")) return "fa-opera";
        else return "fa-globe";
    }

}
