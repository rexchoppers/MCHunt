package com.rexchoppers.mchunt.signs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.HashMap;
import java.util.Map;

public class ScrollingSign {
    private String[] staticMessages;
    private Map<Integer, String> dynamicMessages; // Map line numbers to their messages
    private int[] dynamicLines;
    private Location location;
    private Map<Integer, Integer> lineIndexes; // Track current index for each line
    private Map<Integer, Integer> lineDelays; // Track delay counters for each line

    public ScrollingSign(
            String[] staticMessages,
            Map<Integer, String> dynamicMessages,
            int[] dynamicLines,
            Location location
    ) {
        this.staticMessages = staticMessages;
        this.dynamicMessages = dynamicMessages;
        this.dynamicLines = dynamicLines;
        this.location = location;
        this.lineIndexes = new HashMap<>();
        this.lineDelays = new HashMap<>();
        for (int line : dynamicLines) {
            lineIndexes.put(line, 0);
            lineDelays.put(line, 0);
        }
    }

    public void updateText() {
        if (location == null) {
            return;
        }

        if (!location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return; // Avoid force loading the chunk.
        }

        if (!(location.getBlock().getState() instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) location.getBlock().getState();
        String[] lines = staticMessages.clone(); // Start with a clone of the static messages

        // Handle dynamic line updates without delay
        for (int line : dynamicLines) {
            if (line < lines.length) {
                String fullText = dynamicMessages.get(line);
                int currentIndex = lineIndexes.get(line);
                String visibleText = getVisibleText(fullText, currentIndex, 15); // Max 15 chars on a sign
                lines[line] = visibleText;
                currentIndex = (currentIndex + 1) % fullText.length();
                lineIndexes.put(line, currentIndex);
            }
        }

        // Apply updated text to the sign
        for (int i = 0; i < lines.length; i++) {
            sign.setLine(i, lines[i]);
        }
        sign.update(true, false); // Force the sign update without block physics update
    }

    private String getVisibleText(String text, int startIndex, int length) {
        if (text.length() <= length) {
            return text;
        }
        // Handle text wrapping around the end
        if (startIndex + length > text.length()) {
            int endIndex = startIndex + length - text.length();
            return text.substring(startIndex) + text.substring(0, endIndex);
        } else {
            return text.substring(startIndex, startIndex + length);
        }
    }
}
