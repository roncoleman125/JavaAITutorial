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
package ann.io;

/**
 * Flower class
 * @author Ron Coleman
 */
public class Flower {
    protected Double sepalLength;
    protected Double sepalWidth;
    protected Double petalLength;
    protected Double petalWidth;
    protected Species species;

    /**
     * Constructor
     * @param sepalLength Sepal length
     * @param sepalWidth Sepal width
     * @param petalLength Petal length
     * @param petalWidth Petal width
     * @param species Species
     */
    public Flower(Double sepalLength, Double sepalWidth, Double petalLength, Double petalWidth, Species species) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
        this.species = species;
    }

    /**
     * Renders the string name of this class as the species.
     * @return String name
     */
    @Override
    public String toString() {
        return this.species.toString();
    }
}
