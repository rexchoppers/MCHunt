package com.rexchoppers.mchunt.util;

public class Format {
    private static final String ARG = "&3";
    private static final String ERROR = "&c";
    private static final String HEADER = "&6";
    private static final String NORMAL = "&f";
    private static final String TEXT = "&f";
    private static final String TAG = "%a[%nMCHunt%a]%n";
    private static final String WARNING = "&4";
    private static final String GOOD = "&a";
    private static final String BAD = "&c";
    private static final String BOLD = "&l";

    public static String processString(String message) {
        return message.replaceAll("%TAG", TAG)
                .replaceAll("%n", NORMAL)
                .replaceAll("%w", WARNING)
                .replaceAll("%e", ERROR)
                .replaceAll("%a", ARG)
                .replaceAll("%t", TEXT)
                .replaceAll("%h", HEADER)
                .replaceAll("%g", GOOD)
                .replaceAll("%b", BAD)
                .replaceAll("%B", BOLD)
                .replaceAll("(&([a-fk-or0-9]))", "\u00A7$2")
                .replaceAll("&u", "\n");
    }
}
