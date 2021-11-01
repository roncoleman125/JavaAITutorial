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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A route with a collection of stops.
 * @author Ron.Coleman
 */
public class Route {
    private List<Address> stops;
    private int distance;

    /**
     * Copy constructor
     * @param stops Stops on route
     */
    public Route(List<Address> stops) {
        this.stops = new ArrayList<>(stops);
//        Collections.shuffle(this.stops);
    }

    /**
     * Gets stop at index.
     * @param index Stop index
     * @return Stop
     */
    public Address getStop(int index) {
        assert(index < stops.size());
        return stops.get(index);
    }

    /**
     * Gets the route distance.
     * @return
     */
    public int getDist() {
        if (distance != 0) return distance;

        distance = 0;

        int numStops = getNumStops();
        for (int i = 0; i < numStops; i++) {
            Address start = getStop(i);
            Address end = getStop(i+1 < numStops ? i+1 : 0);
            distance += Address.distance(start, end);
        }

        return distance;
    }

    /**
     * Deep copies route
     * @return Route copy
     */
    public Route copy() {
        return new Route(new ArrayList<>(stops));
    }

    public List<Address> getStops() {
        return stops;
    }

    /**
     * Gets number of stops on the route.
     * @return Number of stops
     */
    public int getNumStops() {
        return stops.size();
    }

    /**
     * Gets string representation of route.
     * @return String encoding
     */
    @Override
    public String toString() {
        String s = "";
        for(Address address : stops) {
            s += String.format("stop(%d, %d) -> ", address.getX(), address.getY());
        }
        s += "@";
        return s;
    }
}
