package codox.uk.mchunt.util;

import org.bukkit.Bukkit;

public class GeneralUtility {
    public static void sendConsoleMessage(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }
}
