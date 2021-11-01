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

import java.util.Random;

/**
 * Convenience class
 * @author Ron.Coleman
 */
public class Util {
    private final static Random ran = new Random(0);
    /**
     * Gets the probability of jumping into a higher energy state, from s1 -> s2.
     * @param state1 State 1
     * @param state2 State 2
     * @param temp At this temperature
     * @return
     */
    public static double probability(double state1, double state2, double temp) {
        if (state2 < state1)
            return 1;
        return Math.exp(-(state2 - state1) / temp);
    }

    public static int getRandomIndex(int startIdx, int endIdx) {
        int range = (endIdx - startIdx);
        int index = (int) (startIdx + ran.nextDouble()*range);
        return index;
    }
}
