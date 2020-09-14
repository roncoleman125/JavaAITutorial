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
package javaai.metah;

import java.util.Random;

/**
 * This class uses Monte Carlo simulation for unsupervised learning to maximize:
 * f(x,y)=e^-(x^2 + y^2) + 2 * e^-((x-1.7)^2 + (y-1.7)^2).
 * @see "<a href=https://en.wikipedia.org/wiki/Hill_climbing>Hill climbing</a>"
 * @see "<a href=http://modelai.gettysburg.edu/2014/mc1/index.html>An Introduction to Monte Carlo Techniques in Artificial Intelligence - Part I</a>"
 */

public class MonteCarlo {
    /** Convergence threshold */
    public final static double TOLERANCE = 0.01;

    /** Number of samples */
    public final static int NUM_SAMPLES = 10000;

    /** Maximum guess constraint */
    public final static double MAX = 10.0;

    /** Minimum guess constraint */
    public final static double MIN = -10.0;

    /** Convergence criteria: number of times best solution stays best */
    public final static int MAX_SAME_COUNT = 5;

    /** X index in samples array */
    public final static int X = 0;

    /** Y index in samples array */
    public final static int Y = 1;

    /** Z index in samples array */
    public final static int Z = 2;

    /** Random number generator */
    Random ran = new Random();

    /** Same count for test of convergence */
    protected int sameCount = 0;

    /** Last z value of training iteration */
    protected double zLast;

    /**
     * App launch point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        MonteCarlo mc = new MonteCarlo();

        double[] best = mc.solve();

        System.out.printf("best x=%6.4f y=%6.4f z=%f\n",best[X], best[Y], best[Z]);
    }

    /**
     * Solves the equation.
     * @return Heuristic best solution
     */
    public double[] solve() {
        // Epoch count
        int iteration = 0;

        // Initial sample
        double[] sample = {getSample(), getSample(), -Double.MAX_VALUE};

        // Current best
        double[] best = {sample[X], sample[Y], evaluate(sample)};

        // Write report header.
        System.out.printf("%-3s  %6s  %6s  %6s  %5s\n","#", "X", "Y", "Z", "same");

        // Loop until solution converges
        boolean converged = false;

        while(!converged) {
            iteration++;

            // Evaluate each sample in turn
            for(int k = 0; k < NUM_SAMPLES; k++) {
                sample[X] = getSample();
                sample[Y] = getSample();

                sample[Z] = evaluate(sample);

                // If this z is better than the best, update best
                if(sample[Z] > best[Z])
                    System.arraycopy(sample,0,best,0,best.length);
            }

            System.out.printf("%-3d  %6.4f  %6.4f  %6.4f  %5d\n",iteration,best[X],best[Y],best[Z], sameCount);

            // Check for convergence
            converged = didConverge(best[Z]);
        }

        return best;
    }

    /**
     * Gets a random sample
     * @return Sample
     */
    protected double getSample() {
        double sample = ran.nextDouble()*(MAX - MIN) + MIN;

        return sample;
    }

    /**
     * Objective function
     * @param sample Parameters
     * @return y=predict(x)
     */
    protected double evaluate(double[] sample) {
        double x = sample[X];
        double y = sample[Y];

        double z = Math.exp(-(x*x + y*y)) + 2.0 * Math.exp(-((x-1.7)*(x-1.7) + (y-1.7)*(y-1.7)));

        return z;
    }

    /**
     * Convergence test function
     * @param z Criteria
     * @return True if the function converged, false otherwise
     */
    protected boolean didConverge(double z) {
        if(sameCount >= MAX_SAME_COUNT)
            return true;

        if(Math.abs(zLast - z) < TOLERANCE) {
            sameCount++;
        }
        else
            sameCount = 0;

        zLast = z;

        return false;
    }
}
