package javaai.cluster;

public class Point {
    public Double d;
    public Point(Point pt) {
        this.d = d;
    }

    static Point getHi() {
        return new Point(Double.MAX_VALUE);
    }

    static Point getLo() {
        return new Point(Double.MIN_VALUE);
    }

    public Point(Double d) {
        this.d = d;
    }

    public Double distanceTo(Point other) {
        return (d - other.d) * (d - other.d);
    }

    public Point add(Point other) {
        return new Point(d + other.d);
    }

    public Point div(Double divisor) {
        return new Point(divisor / this.d);
    }

    public Boolean gt(Point other) {
        return d > other.d;
    }

    public Boolean lt(Point other) {
        return d < other.d;
    }

    public String toString() {
        return "" + d;
    }

}
