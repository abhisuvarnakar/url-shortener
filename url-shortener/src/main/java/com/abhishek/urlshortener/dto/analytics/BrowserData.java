package com.abhishek.urlshortener.dto.analytics;

public class BrowserData {

    private String name;
    private String icon;
    private Long clicks;
    private Double percentage;

    public BrowserData() {
    }

    public BrowserData(String name, String icon, Long clicks, Double percentage) {
        this.name = name;
        this.icon = icon;
        this.clicks = clicks;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
