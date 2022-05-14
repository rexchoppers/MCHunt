package codox.uk.mchunt.util;

public class Format {
    public static String ARG = "&3";
    public static String ERROR = "&c";
    public static String HEADER = "&6";
    public static String NORMAL = "&f";
    public static String TEXT = "&f";
    public static String TAG = "%a[%nVoidships%a]%n";
    public static String WARNING = "&4";
    public static String GOOD = "&a";
    public static String BAD = "&c";
    public static String BOLD = "&l";

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
