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
package javaai.aann;

/**
 * Represents flower measurement.
 * @author Ron Coleman
 */
public class Measure {
    /** Sepal length index */
    public final static int SEP_LENGTH = 0;

    /** Sepal width index */
    public final static int SEP_WIDTH = 1;

    /** Petal length index */
    public final static int PET_LENGTH = 2;

    /** Petal width index */
    public final static int PET_WIDTH = 3;

    /** Measurement values */
    public double[] values = {0, 0, 0, 0};

    /**
     * Constructor
     * @param sepalLength Sepal length
     * @param sepalWidth Sepal width
     * @param petalLength Petal length
     * @param petalWidth Petal width
     */
    public Measure(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth) {
        this.values[SEP_LENGTH] = sepalLength;
        this.values[SEP_WIDTH] = sepalWidth;
        this.values[PET_LENGTH] = petalLength;
        this.values[PET_WIDTH] = petalWidth;
    }

    /**
     * Returns string representation.
     * @return String
     */
    @Override
    public String toString() {
        return "("+ values[SEP_LENGTH]+", "+ values[SEP_WIDTH]+", "+ values[PET_LENGTH]+", "+ values[PET_WIDTH]+")";
    }
}
