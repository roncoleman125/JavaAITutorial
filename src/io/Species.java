package io;

public abstract class Species {
    @Override
    public String toString() {
        return this.getClass().getName();
    }
}
