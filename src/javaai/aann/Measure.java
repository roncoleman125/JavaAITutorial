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

import java.util.Random;

/**
 * Represents a flower measurement.
 * @author Ron Coleman
 */
public class Measure {
    public final static int SEP_LENGTH = 0;
    public final static int SEP_WIDTH = 1;
    public final static int PET_LENGTH = 2;
    public final static int PET_WIDTH = 3;

    public double[] values = {0, 0, 0, 0};
    protected static Random ran = null;

    // These ranges were learned from NormalizedIris.java
    double[][] ranges = {
            {4.30, 7.50},  // Sepal length
            {2.00, 4.40},  // Sepal width
            {1.00, 6.90},  // Petal length
            {0.10, 2.50}   // Petal width
    } ;

    /**
     * Constructor
     */
    public Measure() {
        ran = new Random();
        init();
    }

    /**
     * Constructor for a random seeded measure.
     * @param seed Random seed
     */
    public Measure(long seed) {
       ran = new Random(seed);
       init();
    }

    /**
     * Initializes a measure.
     */
    protected final void init() {
        // Generate plausible random values.
        for(int k=0; k < values.length; k++) {
            double[] range = ranges[k];

            // See https://stackoverflow.com/questions/3680637/generate-a-random-double-in-a-range
            double randomValue = range[0] + (range[1] - range[0]) * ran.nextDouble();
            values[k] = randomValue;
        }
    }

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
        String s = String.format("(%4.2f, %4.2f, %4.2f, %4.2f)",
                values[SEP_LENGTH],
                values[SEP_WIDTH],
                values[PET_LENGTH],
                values[PET_WIDTH]);
        return s;
    }

    /**
     * Compares this measure with another.
     * @param obj Measure to be compared with this one.
     * @return True if measures are equal.
     */
    @Override
    public boolean equals(Object obj) {
        // Make sure the object is a measure.
        if(obj == null || !(obj instanceof Measure))
            return false;

        // Two measures are equal only if all their values are equal.
        Measure other = (Measure) obj;

        return values[SEP_LENGTH] == other.values[SEP_LENGTH] &&
               values[SEP_WIDTH] == other.values[SEP_WIDTH] &&
               values[PET_LENGTH] == other.values[PET_LENGTH] &&
               values[PET_WIDTH] == other.values[PET_WIDTH];
    }
}
