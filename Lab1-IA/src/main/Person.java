package main;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return index == person.index && isMale == person.isMale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, isMale);
    }
}
