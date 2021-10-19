package main;

import java.util.List;

public class Region {
    private final int index;
    private final String name;
    private Color finalColor = Color.UNDETERMINED;
    private final List<Color> possibleColors;

    public Region(int index, String name, List<Color> possibleColors) {
        this.index = index;
        this.name = name;
        this.possibleColors = possibleColors;
    }

    public Color getFinalColor() {
        return finalColor;
    }

    public void setFinalColor(Color finalColor) {
        this.finalColor = finalColor;
    }
}
