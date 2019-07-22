package io;

public class Flower implements Comparable<Flower> {
    public final static int SEPAL_LENGTH = 0;
    public final static int SEPAL_WIDTH = 1;
    public final static int PETAL_LENGTH = 2;
    public final static int PETAL_WIDTH = 3;

    protected Integer sepalLength;
    protected Integer sepalWidth;
    protected Integer petalLength;
    protected Integer petalWidth;
    protected Species species;

    public Flower(Integer sepalLength, Integer sepalWidth, Integer petalLength, Integer petalWidth, Species species) {
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
}
