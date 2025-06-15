package com.abhishek.urlshortener.dto;

public class UrlRequestDTO {

    private Long urlId;
    private String originalUrl;
    private String customAlias;
    private Integer extensionDays;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
    }

    public Long getUrlId() {
        return urlId;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public Integer getExtensionDays() {
        return extensionDays;
    }

    public void setExtensionDays(Integer extensionDays) {
        this.extensionDays = extensionDays;
    }
}
