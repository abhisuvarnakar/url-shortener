package com.abhishek.urlshortener.dto.analytics;

public class CountryData {

    private String name;
    private String code;
    private Long clicks;
    private Double percentage;

    public CountryData() {
    }

    public CountryData(String name, String code, Long clicks, Double percentage) {
        this.name = name;
        this.code = code;
        this.clicks = clicks;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
