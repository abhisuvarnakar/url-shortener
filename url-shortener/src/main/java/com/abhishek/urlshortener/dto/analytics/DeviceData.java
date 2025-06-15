package com.abhishek.urlshortener.dto.analytics;

public class DeviceData {

    private String type;
    private String icon;
    private String color;
    private Long clicks;
    private Double percentage;

    public DeviceData() {
    }

    public DeviceData(String type, String icon, String color, Long clicks, Double percentage) {
        this.type = type;
        this.icon = icon;
        this.color = color;
        this.clicks = clicks;
        this.percentage = percentage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
