package com.rexchoppers.mchunt.signs;

import org.bukkit.Location;
import org.bukkit.block.Sign;

public class ScrollingSign {
    private String[] staticMessages;
    private String[] dynamicMessages;
    private int[] dynamicLines;

    private Location location;
    private int index = 0;

    public ScrollingSign(
            String[] staticMessages,
            String[] dynamicMessages,
            int[] dynamicLines,
            Location location
    ) {
        this.staticMessages = staticMessages;
        this.dynamicMessages = dynamicMessages;
        this.dynamicLines = dynamicLines;
        this.location = location;
    }

    public String[] getNextLines() {
        String[] lines = staticMessages.clone();
        for (int dynamicLine : dynamicLines) {
            int lineIndex = (index + dynamicLine) % dynamicMessages.length;
            lines[dynamicLine] = dynamicMessages[lineIndex];
        }
        index = (index + 1) % dynamicMessages.length;
        return lines;
    }

    public void updateText() {
        if (location == null || !location.getChunk().isLoaded()) {
            return; // Ensure the chunk where the sign is located is loaded
        }

        Sign sign = (Sign) location.getBlock().getState(); // Get the sign state
        String[] lines = staticMessages.clone(); // Start with a clone of the static messages

        // Update the dynamic lines with the scrolled text
        for (int lineIndex : dynamicLines) {
            if (lineIndex < lines.length) {
                lines[lineIndex] = dynamicMessages[(index + lineIndex) % dynamicMessages.length];
            }
        }

        // Set the new text on the sign
        for (int i = 0; i < lines.length; i++) {
            sign.setLine(i, lines[i]);
        }
        sign.update(); // Make sure to update the sign to show changes

        // Move to the next index
        index = (index + 1) % dynamicMessages.length;
    }
}
