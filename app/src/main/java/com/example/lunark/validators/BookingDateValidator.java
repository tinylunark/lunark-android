package com.example.lunark.validators;

import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BookingDateValidator implements CalendarConstraints.DateValidator {
    private Calendar mUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private final List<Long> mAvailableDates;

    public static final Creator<BookingDateValidator> CREATOR = new Creator<BookingDateValidator>() {
        @Override
        public BookingDateValidator createFromParcel(Parcel source) {
            return new BookingDateValidator(new ArrayList<>());
        }

        @Override
        public BookingDateValidator[] newArray(int size) {
            return new BookingDateValidator[size];
        }
    };

    public BookingDateValidator(List<Long> availableDates) {
        mAvailableDates = availableDates;
    }

    @Override
    public boolean isValid(long date) {
        return mAvailableDates.contains(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }
}
