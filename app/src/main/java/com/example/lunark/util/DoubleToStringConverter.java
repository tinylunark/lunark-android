package com.example.lunark.util;

import androidx.databinding.InverseMethod;

public class DoubleToStringConverter {
    @InverseMethod("stringToDouble")
    public static String doubleToString(Double doubleValue) {
        if (doubleValue == null) {
            return "";
        } else {
            return doubleValue.toString();
        }
    }

    public static Double stringToDouble(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        } else {
            return Double.parseDouble(string);
        }
    }
}
