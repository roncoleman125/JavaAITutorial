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
package javaai.metah.hill;

import javaai.metah.Direction;

/**
 * This class implements the hill climbing reinforcement learning algorithm to find base point
 * for f(x,y)=e^-(x^2+y^2) + 2*e^-((x-1.7)^2+(y-1.7)^2).
 * @author Ron.Coleman
 */
public class DifficultHillClimbing extends HillClimbing {
    /**
     * Launch point
     * @param args Command line args not used.
     */
    public static void main(String[] args) {
        HillClimbing hill = new DifficultHillClimbing(Direction.MAXIMIZE);

        double[] pt = hill.climb();

        System.out.println("CONVERED best: x="+pt[0]+" y="+pt[1]+" fitness="+hill.getFitness(pt));
    }

    /**
     * Constructor
     * @param direction Direction
     */
    public DifficultHillClimbing(Direction direction) {
        super(direction);
    }

    /**
     * Implements the objective function.
     * @param pt Point in N-space we're testing
     * @return fitness
     */
    @Override
    public double getFitness(double[] pt) {
        double x = pt[0];
        double y = pt[1];

        double result = Math.exp(-(x*x + y*y)) + 2 * Math.exp(-((x-1.7)*(x-1.7) + (y-1.7)*(y-1.7)));

        return result;
    }
}
