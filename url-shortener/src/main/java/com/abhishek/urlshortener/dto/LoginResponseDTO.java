package com.abhishek.urlshortener.dto;

public class LoginResponseDTO {

    private String status;
    private String accessToken;
    private String message;

    public LoginResponseDTO(String status, String accessToken, String message) {
        this.status = status;
        this.accessToken = accessToken;
        this.message = message;
    }

    public LoginResponseDTO() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
