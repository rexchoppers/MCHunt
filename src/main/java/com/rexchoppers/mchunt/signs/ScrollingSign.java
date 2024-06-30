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

        // Correct potential splitting of formatting codes
        visibleText = correctSplitFormattingCodes(visibleText);

        // Maintain continuity of formatting by prepending last known formatting codes
        String lastKnownFormat = lastFormat.getOrDefault(line, "");
        visibleText = lastKnownFormat + visibleText;

        // Update last known formatting based on the new visible text
        lastFormat.put(line, extractLastFormat(visibleText));

        return visibleText;
    }

    private String correctSplitFormattingCodes(String visibleText) {
        int lastFormatPos = visibleText.lastIndexOf('§');
        if (lastFormatPos != -1 && lastFormatPos == visibleText.length() - 1) {
            // If the last character is an incomplete formatting code, remove it
            visibleText = visibleText.substring(0, lastFormatPos);
        } else if (lastFormatPos != -1 && lastFormatPos < visibleText.length() - 1) {
            // Check if the character following § is a valid format code
            if (!isLetterOrColorCode(visibleText.charAt(lastFormatPos + 1))) {
                // If not a valid format code, remove the solitary §
                visibleText = visibleText.substring(0, lastFormatPos) + visibleText.substring(lastFormatPos + 1);
            }
        }
        return visibleText;
    }

    private String extractLastFormat(String text) {
        int lastFormatIndex = text.lastIndexOf("§");
        if (lastFormatIndex != -1 && lastFormatIndex < text.length() - 1) {
            char formatCode = text.charAt(lastFormatIndex + 1);
            if (isLetterOrColorCode(formatCode)) {
                return "§" + formatCode;
            }
        }
        return "";
    }

    private boolean isLetterOrColorCode(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'k' && c <= 'r');
    }
}