package com.rexchoppers.mchunt.signs;

import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScrollingSign {
    private String[] staticMessages;
    private Map<Integer, String> dynamicMessages;
    private int[] dynamicLines;
    private Location location;
    private Map<Integer, Integer> lineIndexes;
    private Map<Integer, String> lastFormat; // This will store the last effective % format, not color code

    public ScrollingSign(String[] staticMessages, Map<Integer, String> dynamicMessages, int[] dynamicLines, Location location) {
        this.staticMessages = staticMessages;
        this.dynamicMessages = dynamicMessages;
        this.dynamicLines = dynamicLines;
        this.location = location;
        this.lineIndexes = new HashMap<>();
        this.lastFormat = new HashMap<>();
        for (int line : dynamicLines) {
            lineIndexes.put(line, 0);
            lastFormat.put(line, "");
        }
    }

    public void updateText() {
        if (location == null || !location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return;
        }

        if (!(location.getBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) location.getBlock().getState();
        String[] lines = staticMessages.clone();

        for (int line : dynamicLines) {
            if (line < lines.length) {
                String fullText = dynamicMessages.get(line);
                int currentIndex = lineIndexes.get(line);
                String visibleText = getVisibleText(fullText, currentIndex, 15, line);
                Bukkit.broadcastMessage("Visible text: " + visibleText);
                lines[line] = visibleText;
                currentIndex = (currentIndex + 1) % fullText.length();
                lineIndexes.put(line, currentIndex);
            }
        }

        for (int i = 0; i < lines.length; i++) {
            sign.setLine(i, lines[i]);
        }
        sign.update(true, false);
    }

    private String getVisibleText(String text, int startIndex, int length, int line) {
        String formattedText = Format.processString(text); // Apply initial formatting
        int endIndex = (startIndex + length) % formattedText.length(); // Circular index for wrapping

        String visibleText;
        if (endIndex < startIndex) { // Wraps around
            visibleText = formattedText.substring(startIndex) + formattedText.substring(0, endIndex);
        } else {
            visibleText = formattedText.substring(startIndex, endIndex);
        }

        // Extract formatting information for wrapping scenarios
        String firstFormat = findFirstFormat(visibleText);
        String lastFormat = findLastFormat(visibleText);

        if (!firstFormat.isEmpty()) {
            this.lastFormat.put(line, firstFormat); // Set the starting format for next wrap
        }

        if (!lastFormat.isEmpty()) {
            this.lastFormat.put(line, lastFormat); // Set the ending format to apply on next scroll
        }

        // Apply the last known format at the beginning of the visible text
        visibleText = this.lastFormat.getOrDefault(line, "") + visibleText;

        return Format.processString(visibleText); // Re-process for any interrupted formatting
    }

    private String findFirstFormat(String text) {
        Matcher matcher = Pattern.compile("(&[0-9a-fk-or])").matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

    private String findLastFormat(String text) {
        Matcher matcher = Pattern.compile("(&[0-9a-fk-or])").matcher(text);
        String lastFound = "";
        while (matcher.find()) {
            lastFound = matcher.group(); // This captures the last format found
        }
        return lastFound;
    }
}