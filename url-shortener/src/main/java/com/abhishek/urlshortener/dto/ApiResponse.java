package com.abhishek.urlshortener.dto;

import com.abhishek.urlshortener.entity.enums.Status;

public class ApiResponse<T> {

    private Status status;
    private T data;
    private String message;

    public ApiResponse() {
    }

    public ApiResponse(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
