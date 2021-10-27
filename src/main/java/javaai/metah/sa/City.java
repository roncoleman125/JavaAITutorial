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
import java.util.List;
import java.util.Random;

/**
 * Represents a city.
 * @author Ron.Coleman
 */
public class City {
    /** Maximum range between cities in x or y coordinate. */
    public final static int RANGE = 20;

    private static Random ran = new Random(0);

    private int x;
    private int y;

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     */
    public City(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Gets the x coordinate.
     * @return X
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y coordinate.
     * @return Y
     */
    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof City) || obj == null)
            return false;
        City city = (City)obj;
        return city.x == this.x && city.y == this.y;
    }

    /**
     * Makes a collection of cities at random locations.
     * @param n Number of cities to make
     * @return List of cities
     */
    public static List<City> make(int n) {
        List<City> cities = new ArrayList<>();
        for(int i=0; i < n; i++) {
            City city = getCity(cities);
            cities.add(city);
        }
        return cities;
    }

    /**
     * Gets a unique city that is not already in the list.
     * @param cities Collection of cities in the list
     * @return Unique city
     */
    private static City getCity(List<City> cities) {
        int x = ran.nextInt(RANGE)+1;
        int y = ran.nextInt(RANGE)+1;
        City city = new City(x,y);
        if(cities.contains(city))
            return getCity(cities);
        return city;
    }

    /**
     * Gets the distance between two cities "as the crow flies".
     * @param city1 City 1
     * @param city2 City 2
     * @return Distance
     */
    public static double distance(City city1, City city2) {
        int xDist = Math.abs(city1.getX() - city2.getX());
        int yDist = Math.abs(city1.getY() - city2.getY());
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }
}
