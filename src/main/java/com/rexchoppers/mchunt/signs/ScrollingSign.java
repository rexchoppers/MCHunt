package com.rexchoppers.mchunt.signs;

import com.rexchoppers.mchunt.util.Format;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;

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
        // Apply custom markers before calling Format.processString
        String formattedText = applyCustomMarkers(text, startIndex, length);

        // Ensure the processed text doesn't lose formatting
        return Format.processString(formattedText);
    }

    private String applyCustomMarkers(String text, int startIndex, int length) {
        StringBuilder visibleText = new StringBuilder();
        String currentFormat = "";

        // Find the first relevant format marker before the visible text starts
        for (int i = startIndex - 1; i >= 0; i--) {
            if (text.charAt(i) == '%') {
                currentFormat = "%" + text.charAt(i + 1);
                break;
            }
        }

        // Build the visible text with the relevant format markers applied
        for (int i = 0; i < length; i++) {
            int currentIndex = (startIndex + i) % text.length();
            char currentChar = text.charAt(currentIndex);

            if (currentChar == '%') {
                currentFormat = "%" + text.charAt((currentIndex + 1) % text.length());
                visibleText.append(currentFormat);
                i++; // Skip the next character as it is part of the custom format code
            } else {
                visibleText.append(currentChar);
            }
        }

        // Reapply the last known format to ensure continuity
        if (!currentFormat.isEmpty()) {
            visibleText.insert(0, currentFormat);
        }

        return visibleText.toString();
    }
}
