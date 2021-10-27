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
package javaai.metah.sa;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Solves travelling salesman problem with simulated annealing.
 * @author Ron.Coleman
 * @see <a href="https://stackabuse.com/simulated-annealing-optimization-algorithm-in-java/">Simulated Annealing Optimization Algorithm in Java</a>
 */
public class Tsp {
    private static double temperature = 1000;
    private static double coolingFactor = 0.995;
    private static Random ran = new Random(0);
    public static final int NUM_CITIES = 10;

    public static void main(String[] args) {
        List<City> cities = City.make(NUM_CITIES);

        Tour current = new Tour(cities);
        Tour best = current.duplicate();

        int epoch = 1;
        for (double t = temperature; t > 1; t *= coolingFactor, epoch++) {
            Tour neighbor = current.duplicate();

            int i = (int) (neighbor.noCities() * Math.random());
            int j = (int) (neighbor.noCities() * Math.random());

//            Collections.swap(next.getCities(), index1, index2);
            Collections.swap(cities, i, j);

            int currentLength = current.getTourLength();
            int neighborLength = neighbor.getTourLength();

            if (Math.random() < Util.probability(currentLength, neighborLength, t)) {
                current = neighbor.duplicate();
            }

            if (current.getTourLength() < best.getTourLength()) {
                best = current.duplicate();
            }
            System.out.println("epoch: "+epoch+" length: "+currentLength+" best: "+best.getTourLength());
        }

        System.out.println("Final tour length: " + best.getTourLength());
        System.out.println("Tour: " + best);
    }
}
