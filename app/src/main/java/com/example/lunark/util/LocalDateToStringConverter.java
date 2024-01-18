package com.example.lunark.util;

import androidx.databinding.InverseMethod;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateToStringConverter {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @InverseMethod("stringToLocalDate")
    public static String localDateToString(LocalDate localDate) {
        if (localDate == null) {
            return "";
        } else {
            return localDate.format(formatter);
        }
    }

    public static LocalDate stringToLocalDate(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        } else {
            try {
                return LocalDate.parse(string, formatter);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
    }
}
