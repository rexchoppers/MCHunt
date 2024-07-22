package com.rexchoppers.mchunt.util;

public class TimeUtil {
    public static String formatTime(int totalSeconds) {
        int days = totalSeconds / 86400;
        int hours = (totalSeconds % 86400) / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        StringBuilder timeString = new StringBuilder();

        if (days > 0) {
            timeString.append("%a").append(days).append("%n").append("d ");
        }

        if (hours > 0 || days > 0) {
            timeString.append("%a").append(hours).append("%n").append("h ");
        }

        if (minutes > 0 || hours > 0 || days > 0) {
            timeString.append("%a").append(minutes).append("%n").append("m ");
        }
        timeString.append("%a").append(seconds).append("%n").append("s");

        return Format.processString(timeString.toString().trim());
    }
}
