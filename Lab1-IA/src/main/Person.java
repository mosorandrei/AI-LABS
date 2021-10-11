package main;

public class Person {
    private final int index;
    private boolean isMale;

    public Person(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }
}
