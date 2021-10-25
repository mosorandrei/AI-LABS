package main;

import java.util.Objects;

public class GamePiece {
    int colorIndex; // has to be less or equal to k

    public GamePiece(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    @Override
    public String toString() {
        return "[COLOR #" + colorIndex + "]GamePiece";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePiece gamePiece = (GamePiece) o;
        return colorIndex == gamePiece.colorIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorIndex);
    }
}
