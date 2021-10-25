package main;

import java.util.List;

public class Region {
    private final int index;
    private final String name;
    private Color finalColor = Color.UNDETERMINED;
    public List<Color> possibleColors;

    public Region(int index, String name, List<Color> possibleColors) {
        this.index = index;
        this.name = name;
        this.possibleColors = possibleColors;
    }

    public void removeColor(Color color){
        this.possibleColors.remove(color);
    }

    public void addColor(Color color){
        this.possibleColors.add(color);
    }

    public Color getFinalColor() {
        return finalColor;
    }

    public void setFinalColor(Color finalColor) {
        this.finalColor = finalColor;
    }

    public List<Color> getPossibleColors() {
        return possibleColors;
    }

    @Override
    public String toString() {
        return "[Region #" + index + "] " + name + " - " + finalColor;
    }
}