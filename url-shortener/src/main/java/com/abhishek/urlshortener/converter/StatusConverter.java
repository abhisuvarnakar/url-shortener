package com.abhishek.urlshortener.converter;

import com.abhishek.urlshortener.entity.enums.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        return status != null ? status.name() : null;
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        return s != null ? Status.valueOf(s.toUpperCase()) : null;
    }
}
