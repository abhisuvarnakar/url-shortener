package com.abhishek.urlshortener.dto;

import com.abhishek.urlshortener.entity.enums.Status;

import java.util.Date;

public class UrlResponseDTO {

    private Long id;
    private String shortCode;
    private String originalUrl;
    private Long clickCount;
    private Date createdAt;
    private Date expiresAt;
    private Status status;

    public UrlResponseDTO() {
    }

    public UrlResponseDTO(Long id, String shortCode, String originalUrl,
                          Long clickCount, Date createdAt, Date expiresAt, Status status) {
        this.id = id;
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
