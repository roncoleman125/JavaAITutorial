package io;

public class Flower implements Comparable<Flower> {
    protected Double sepalLength;
    protected Double sepalWidth;
    protected Double petalLength;
    protected Double petalWidth;
    protected Species species;

    public Flower(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth, Species species) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.species = species;
    }

    @Override
    public int compareTo(Flower flower) {
        return 0;
    }

    @Override
    public String toString() {
        return this.species.toString();
    }
}
