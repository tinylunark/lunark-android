package com.example.lunark.util;

import androidx.databinding.InverseMethod;

public class IntegerToStringConverter {
    @InverseMethod("stringToInteger")
    public static String integerToString(Integer integer) {
        if (integer == null) {
            return "";
        } else {
            return integer.toString();
        }
    }

    public static Integer stringToInteger(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        } else {
            return Integer.parseInt(string);
        }
    }
}
