package com.example.lunark.util;

import android.widget.RatingBar;

import androidx.databinding.InverseMethod;

public class FloatToIntegerConverter {

    @InverseMethod("FloatToInteger")
    public static float IntegerToFloat(Integer value) {
        return value;
    }

    public static Integer FloatToInteger(float value) {
        return (int) value;
    }
}
