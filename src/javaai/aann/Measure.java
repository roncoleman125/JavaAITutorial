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
    public Double sepalLength;
    public Double sepalWidth;
    public Double petalLength;
    public Double petalWidth;

    public Measure(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
    }

    /**
     * Gets the sepal length.
     * @return sepal length
     */
    public Double getSepalLength() {
        return sepalLength;
    }

    /**
     * Gets the sepal width.
     * @return sepal width
     */
    public Double getSepalWidth() {
        return sepalWidth;
    }

    /**
     * Gets the petal length.
     * @return Petal length
     */
    public Double getPetalLength() {
        return petalLength;
    }

    /**
     * Gets the petal width.
     * @return Petal width
     */
    public Double getPetalWidth() {
        return petalWidth;
    }

    /**
     * Returns string representation.
     * @return String
     */
    @Override
    public String toString() {
        return "("+sepalLength+", "+sepalWidth+", "+petalLength+", "+petalWidth+")";
    }
}
