/*
 Copyright (c) Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package javaai.cluster;

/**
 * Represents a 1D point.
 * @author Ron.Coleman
 */
public class Point1D {
    public Double x;

    /**
     * Gets the highest point.
     * @return High point
     */
    static Point1D getHi() {
        return new Point1D(Double.MAX_VALUE);
    }

    /**
     * Gets the lowest point.
     * @return Low point
     */
    static Point1D getLo() {
        return new Point1D(Double.MIN_VALUE);
    }

    /**
     * Constructor
     * @param x One point.
     */
    public Point1D(Double x) {
        this.x = x;
    }

    /**
     * Copy constructor
     * @param pt Point to copy
     */
    public Point1D(Point1D pt) {
        this.x = pt.x;
    }

    /**
     * Calculates distance between this point and other.
     * @param other Other point
     * @return Distance metric.
     */
    public Double distanceTo(Point1D other) { return (x-other.x) * (x-other.x); }

    /**
     * Functionally adds particle to this one.
     * @param other Other particle
     * @return New particle.
     */
    public Point1D add(Point1D other) {
        return new Point1D(x + other.x);
    }

    /**
     * Functionally adds particle to this one.
     * @param other Other particle
     * @return New particle.
     */
    public Point1D sub(Point1D other) { return sub(other.x); };

    /**
     * Functionally subtracts scalar from this particle.
     * @param d Scalar
     * @return New particle.
     */
    public Point1D sub(double d) { return new Point1D(x-d);}

    /**
     * Functionally multiplies particle by scalar.
     * @param d Scalar.
     * @return New particle.
     */
    public Point1D mult(double d) { return new Point1D(x*d);}

    /**
     * Functionally divides particle by scalar.
     * @param d Scalar.
     * @return New particle.
     */
    public Point1D div(Double d) { return new Point1D(this.x /d); }

    /**
     * Tests if this particle is grater than another.
     * @param other Other particle
     * @return True if this particle greater than other.
     */
    public Boolean gt(Point1D other) { return x > other.x; }

    /**
     * Tests if this particle is less than another.
     * @param other Other particle
     * @return True if this particle less than other.
     */
    public Boolean lt(Point1D other) { return x < other.x; }

    /**
     * Makes a zero point.
     * @return New particle.
     */
    public static Point1D zero() { return new Point1D(0.0); }

    /**
     * Converts particle to a string.
     * @return String
     */
    @Override
    public String toString() { return String.format("%6.4f", x); }
}
