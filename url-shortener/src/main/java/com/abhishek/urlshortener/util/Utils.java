package com.abhishek.urlshortener.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null || StringUtils.isBlank(pattern)) {
            return "";
        }
        LocalDateTime localDateTime =
                date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        return localDateTime.format(formatter);
    }

}
