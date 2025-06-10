package com.abhishek.urlshortener.dto;

import com.abhishek.urlshortener.entity.enums.Status;

import java.util.Date;

public class ExpiringUrlDTO extends UrlResponseDTO {

    private long daysLeft;

    public ExpiringUrlDTO() {
    }

    public ExpiringUrlDTO(Long id, String shortCode, String originalUrl, Long clickCount,
                          Date createdAt, Date expiresAt, Status status, long daysLeft) {
        super(id, shortCode, originalUrl, clickCount, createdAt, expiresAt, status);
        this.daysLeft = daysLeft;
    }

    public long getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(long daysLeft) {
        this.daysLeft = daysLeft;
    }
}
