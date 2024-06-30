package com.rexchoppers.mchunt.signs;

public class ScrollingSign {
    private String[] staticMessages;
    private String[] dynamicMessages;
    private int[] dynamicLines;
    private int index = 0;

    public ScrollingSign(String[] staticMessages, String[] dynamicMessages, int[] dynamicLines) {
        this.staticMessages = staticMessages;
        this.dynamicMessages = dynamicMessages;
        this.dynamicLines = dynamicLines;
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
}
