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
 * Solves package delivery service (PDS) problem with simulated annealing.
 * @author Ron.Coleman
 * @see <a href="https://stackabuse.com/simulated-annealing-optimization-algorithm-in-java/">Simulated Annealing Optimization Algorithm in Java</a>
 */
public class Pds {
    public static final int NUM_STOPS = Integer.parseInt(System.getProperty("n","4"));
    public final static double COOLING_FACTOR = 0.995;
    public final static double INITIAL_TEMP = 1000;

    private static Random ran = new Random(0);

    public static void main(String[] args) {
        // Make some stops
        List<Address> stops = Address.make(NUM_STOPS);

        // Current & best route are based on the stops
        Route current = new Route(stops);
        Route best = current.copy();

        System.out.printf("%5s %4s %4s %s\n","epoch","best","dist","route");
        int epoch = 1;
        for (double temp = INITIAL_TEMP; temp > 1; temp *= COOLING_FACTOR, epoch++) {
            // Get a route to manipulate
            Route route = current.copy();

            // Swap a pair of stops on this route
//            int i = (int) (route.getNumStops() * Math.random());
//            int j = (int) (route.getNumStops() * Math.random());
            int i = Util.getRandomIndex(1,route.getNumStops());
            int j = Util.getRandomIndex(1,route.getNumStops());

            Collections.swap(route.getStops(), i, j);

            // If the distance is further than the current dist, accept it with P(dE)
            int curDist = current.getDist();
            int routeDist = route.getDist();

            if (Math.random() < Util.probability(curDist, routeDist, temp)) {
                current = route.copy();
            }

            // Update the best if the current is better
            if (current.getDist() < best.getDist()) {
                best = current.copy();
            }

            if(epoch <= 3 || (epoch % 100) == 0)
                System.out.printf("%5d %4d %4d %s\n",epoch,best.getDist(),route.getDist(),route);
        }

        System.out.printf("%5d %4d %4d %s\n",epoch,best.getDist(),best.getDist(),best);

        System.out.println("best route dist: " + best.getDist()+" best: "+best);
//        System.out.println("route: " + best);
    }
}
