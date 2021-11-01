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
 * Represents an address or stop on a route.
 * @author Ron.Coleman
 */
public class Address {
    /** Maximum range between addresses in x or y coordinate. */
    public final static int RANGE = 20;

    private static Random ran = new Random(0);

    private int x;
    private int y;

    /**
     * Constructor
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Address(int x, int y) {
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

    /**
     * Tests of another address is equal
     * @param obj Address object
     * @return True if obj is equal to this address, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Address))
            return false;
        Address address = (Address)obj;
        return address.x == this.x && address.y == this.y;
    }

    /**
     * Represents in string form.
     * @return String
     */
    @Override
    public String toString() {
        return  "("+x+","+y+")";
    }

    /**
     * Makes a collection of addresses at random locations.
     * @param n Number of addresses to make
     * @return List of addresses
     */
    public static List<Address> make(int n) {
        List<Address> addresses = new ArrayList<>();
        for(int i=0; i < n; i++) {
            Address address = getAddress(addresses);
            addresses.add(address);
        }
        return addresses;
    }

    /**
     * Gets a unique address that is not already in the list.
     * @param addresses Collection of addresses in the list
     * @return Unique city
     */
    private static Address getAddress(List<Address> addresses) {
        int x = ran.nextInt(RANGE)+1;
        int y = ran.nextInt(RANGE)+1;
        Address address = new Address(x,y);
        if(addresses.contains(address))
            return getAddress(addresses);
        return address;
    }

    /**
     * Gets the distance between two addresses using taxi cab geometry.
     * @param addr1 Address 1
     * @param addr2 Address 2
     * @return Distance
     */
    public static double distance(Address addr1, Address addr2) {
        int xDist = Math.abs(addr1.getX() - addr2.getX());
        int yDist = Math.abs(addr1.getY() - addr2.getY());
        return (xDist + yDist); //Math.sqrt(xDist * xDist + yDist * yDist);
    }
}
