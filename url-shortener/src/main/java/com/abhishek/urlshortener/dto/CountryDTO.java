package com.abhishek.urlshortener.dto;

public class CountryDTO {

    private String code;
    private String name;

    public CountryDTO() {

    }

    public CountryDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
