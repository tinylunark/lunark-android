package com.example.lunark.util;

import java.time.ZonedDateTime;

public class RelativeDateConverter {
    public static String convertToRelativeString(ZonedDateTime dateTime) {
        long msPerSecond = 1000;
        long msPerMinute = 60 * 1000;
        long msPerHour = msPerMinute * 60;
        long msPerDay = msPerHour * 24;
        long msPerMonth = msPerDay * 30;
        long msPerYear = msPerDay * 365;

        long currentTimeStamp = ZonedDateTime.now().toInstant().toEpochMilli();
        double elapsed = currentTimeStamp - dateTime.toInstant().toEpochMilli();

        if (elapsed < msPerSecond) {
            return "just now";
        }
        if (elapsed < msPerMinute) {
            return Math.round(elapsed / 1000) + " seconds ago";
        }
        else if (elapsed < msPerHour) {
            return Math.round(elapsed / msPerMinute) + " minutes ago";
        }
        else if (elapsed < msPerDay) {
            return Math.round(elapsed / msPerHour) + " hours ago";
        }
        else if (elapsed < msPerMonth) {
            return "approximately " + Math.round(elapsed / msPerDay) + " days ago";
        }
        else if (elapsed < msPerYear) {
            return "approximately " + Math.round(elapsed / msPerMonth) + " months ago";
        }
        else {
            return "approximately " + Math.round(elapsed / msPerYear) + " years ago";
        }
    }
}
