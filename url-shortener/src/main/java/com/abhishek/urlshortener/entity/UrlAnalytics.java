package com.abhishek.urlshortener.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_url_analytics", indexes = {
        @Index(name = "idx_url_analytics1", columnList = "url_id"),
        @Index(name = "idx_url_analytics2", columnList = "click_time"),
        @Index(name = "idx_url_analytics3", columnList = "ip_address")
})
public class UrlAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_analytics_seq_gen")
    @SequenceGenerator(name = "url_analytics_seq_gen", sequenceName = "seq_url_analytics",
            allocationSize = 1, initialValue = 1)
    @Column(name = "analytics_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShortenedUrl shortenedUrl;

    @Column(name = "click_time")
    private Date clickTime;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "country", length = 50)
    private String country;

    @Column(name = "referrer", columnDefinition = "TEXT")
    private String referrer;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "device_type", length = 30)
    private String deviceType;

    @Column(name = "browser", length = 50)
    private String browser;

    @Column(name = "os", length = 50)
    private String operatingSystem;

    @Column(name = "platform", length = 50)
    private String platform;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShortenedUrl getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(ShortenedUrl shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public Date getClickTime() {
        return clickTime;
    }

    public void setClickTime(Date clickTime) {
        this.clickTime = clickTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
