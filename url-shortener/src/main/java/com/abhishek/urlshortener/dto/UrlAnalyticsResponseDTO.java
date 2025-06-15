package com.abhishek.urlshortener.dto;

import com.abhishek.urlshortener.dto.analytics.BrowserData;
import com.abhishek.urlshortener.dto.analytics.CountryData;
import com.abhishek.urlshortener.dto.analytics.DeviceData;

import java.util.List;

public class UrlAnalyticsResponseDTO {

    private String shortUrl;
    private String originalUrl;
    private Long totalClicks;
    private Long todayClicks;
    private Double todayChange;
    private Long weekClicks;
    private Double weekChange;
    private Long monthClicks;
    private Double monthChange;
    private List<String> chartLabels;
    private List<Long> chartData;
    private List<CountryData> topCountries;
    private List<DeviceData> deviceTypes;
    private List<BrowserData> browsers;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(Long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public Long getTodayClicks() {
        return todayClicks;
    }

    public void setTodayClicks(Long todayClicks) {
        this.todayClicks = todayClicks;
    }

    public Double getTodayChange() {
        return todayChange;
    }

    public void setTodayChange(Double todayChange) {
        this.todayChange = todayChange;
    }

    public Long getWeekClicks() {
        return weekClicks;
    }

    public void setWeekClicks(Long weekClicks) {
        this.weekClicks = weekClicks;
    }

    public Double getWeekChange() {
        return weekChange;
    }

    public void setWeekChange(Double weekChange) {
        this.weekChange = weekChange;
    }

    public Long getMonthClicks() {
        return monthClicks;
    }

    public void setMonthClicks(Long monthClicks) {
        this.monthClicks = monthClicks;
    }

    public Double getMonthChange() {
        return monthChange;
    }

    public void setMonthChange(Double monthChange) {
        this.monthChange = monthChange;
    }

    public List<String> getChartLabels() {
        return chartLabels;
    }

    public void setChartLabels(List<String> chartLabels) {
        this.chartLabels = chartLabels;
    }

    public List<Long> getChartData() {
        return chartData;
    }

    public void setChartData(List<Long> chartData) {
        this.chartData = chartData;
    }

    public List<CountryData> getTopCountries() {
        return topCountries;
    }

    public void setTopCountries(List<CountryData> topCountries) {
        this.topCountries = topCountries;
    }

    public List<DeviceData> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<DeviceData> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public List<BrowserData> getBrowsers() {
        return browsers;
    }

    public void setBrowsers(List<BrowserData> browsers) {
        this.browsers = browsers;
    }
}
