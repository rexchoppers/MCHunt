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

    public ScrollingSign(String[] staticMessages, Map<Integer, String> dynamicMessages, int[] dynamicLines, Location location) {
        this.staticMessages = staticMessages;
        this.dynamicMessages = dynamicMessages;
        this.dynamicLines = dynamicLines;
        this.location = location;
        this.lineIndexes = new HashMap<>();
        for (int line : dynamicLines) {
            lineIndexes.put(line, 0);
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
                String visibleText = getVisibleText(fullText, currentIndex, 15);
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

    private String getVisibleText(String text, int startIndex, int length) {
        // Append a space to the text to create a gap when wrapping
        text = text + " ";

        // Ensure the processed text doesn't lose formatting
        text = Format.processString(text);

        // Calculate the visible text indices ensuring circular text scrolling
        int endIndex = (startIndex + length) % text.length();
        String visibleText;
        if (endIndex < startIndex) { // Wraps around
            visibleText = text.substring(startIndex) + text.substring(0, endIndex);
        } else {
            visibleText = text.substring(startIndex, Math.min(startIndex + length, text.length()));
        }

        // Get the first format code from the full text
        String firstFormat = extractFirstFormat(text);
        if (!firstFormat.isEmpty()) {
            visibleText = firstFormat + visibleText;
        }

        return visibleText;
    }

    private String extractFirstFormat(String text) {
        // Pattern to find color codes in the text
        Pattern pattern = Pattern.compile("§[0-9a-fk-or]");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
}
