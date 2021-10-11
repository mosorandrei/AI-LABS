package main;

import java.util.List;
import java.util.Objects;

public class State {
    private final boolean isBoatLeft;
    private final List<Male> leftMales;
    private final List<Female> leftFemales;
    private final List<Male> rightMales;
    private final List<Female> rightFemales;

    public State(List<Male> leftMales, List<Female> leftFemales, boolean isBoatLeft, List<Male> rightMales, List<Female> rightFemales) {
        this.leftMales = leftMales;
        this.leftFemales = leftFemales;
        this.isBoatLeft = isBoatLeft;
        this.rightMales = rightMales;
        this.rightFemales = rightFemales;
    }

    public List<Male> getLeftMales() {
        return leftMales;
    }

    public List<Male> getRightMales() {
        return rightMales;
    }

    public List<Female> getLeftFemales() {
        return leftFemales;
    }

    public List<Female> getRightFemales() {
        return rightFemales;
    }

    public boolean isBoatLeft() {
        return isBoatLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return isBoatLeft == state.isBoatLeft && Objects.equals(leftMales, state.leftMales) && Objects.equals(leftFemales, state.leftFemales) && Objects.equals(rightMales, state.rightMales) && Objects.equals(rightFemales, state.rightFemales);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isBoatLeft, leftMales, leftFemales, rightMales, rightFemales);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("STATUE STATUS").append("\n");
        builder.append("-------------------").append("\n");
        builder.append("Males from left: ");
        for (Male m : leftMales) {
            builder.append(m.getIndex()).append(" ");
        }
        builder.append("\n");
        builder.append("Females from left: ");
        for (Female f : leftFemales) {
            builder.append(f.getIndex()).append(" ");
        }
        builder.append("\n");
        builder.append("Boat position: ");
        if (this.isBoatLeft)
            builder.append("LEFT");
        else
            builder.append("RIGHT");
        builder.append("\n");
        builder.append("Males from right: ");
        for (Male m : rightMales) {
            builder.append(m.getIndex()).append(" ");
        }
        builder.append("\n");
        builder.append("Females from right: ");
        for (Female f : rightFemales) {
            builder.append(f.getIndex()).append(" ");
        }
        return builder.toString();
    }
}
