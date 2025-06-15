package com.abhishek.urlshortener.dto;

public class UserAgentInfoDTO {

    private String deviceType;
    private String browser;
    private String operatingSystem;
    private String platform;

    public UserAgentInfoDTO() {
    }

    public UserAgentInfoDTO(String deviceType, String browser, String operatingSystem,
                            String platform) {
        this.deviceType = deviceType;
        this.browser = browser;
        this.operatingSystem = operatingSystem;
        this.platform = platform;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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
