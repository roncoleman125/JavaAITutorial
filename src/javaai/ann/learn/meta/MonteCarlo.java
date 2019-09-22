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
package javaai.ann.learn.meta;

import java.util.Random;

/**
 * This class uses MC simulation for unsupervised learning to solve y = (x-3)^2.
 * @author Ron.Coleman
 */
public class MonteCarlo {
    /** Convergence threshold */
    public final static double TOLERANCE = 0.01;

    /** Maximum guess */
    public final static int MAX = 128;

    /** Minimum guess */
    public final static int MIN = 0;

    /** Total sample size */
    public final static int SAMPLE_SIZE = 128;

    /** Convergence criteria: number of times best solution stays best */
    public final static int MAX_SAME_COUNT = 5;

    /** Random sample container */
    double[] samples = null;

    /** Random number generator */
    Random ran = new Random(0);

    /** Same count for test of convergence */
    protected int sameCount = 0;

    /** Last y value of training iteration */
    protected double yLast;

    /**
     * Launch point of app.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        MonteCarlo parabola = new MonteCarlo();

        double best = parabola.solve();
        System.out.println("best = "+best);
    }

    /**
     * Solves the equation.
     * @return Heuristic best solution
     */
    public double solve() {
        // Epoch count
        int iteration = 0;

        // Current best y
        double besty = Double.MAX_VALUE;

        // Corresponding best x
        double bestx = 0;

        boolean converged = false;

        // Loop until solution converges
        while(!converged) {
            iteration++;

            double y  = Double.MAX_VALUE;

            // Randomly sample the space
            randomize();

            // Test each sample in turn
            for(int k = 0; k < SAMPLE_SIZE; k++) {
                // Compute y = predict(x)
                double x = samples[k];

                y = getFitness(x);

                // If this y is better than the best, update the y and x
                if(y < besty) {
                    besty = y;
                    bestx = samples[k];
                }
            }

            System.out.printf("%d y=%4.2f same=%d >> %s\n",iteration, besty, sameCount, bestx);

            // Check for convergence
            converged = didConverge(besty);
        }

        return bestx;
    }


    /**
     * Objective function
     * @param x Parameter
     * @return y=predict(x)
     */
    protected double getFitness(double x) {
        return (x-3)*(x-3);
    }

    /**
     * Convergence test function
     * @param y Criteria
     * @return True if the function converged, false otherwise
     */
    protected boolean didConverge(double y) {
        if(sameCount >= MAX_SAME_COUNT)
            return true;

        if(Math.abs(yLast - y) < TOLERANCE) {
            sameCount++;
        }
        else
            sameCount = 0;

        yLast = y;

        return false;
    }

    /**
     * Randomly samples with replacement.
     */
    protected void randomize() {
        // Initialize the samples
        samples = new double[SAMPLE_SIZE];

        // Draw n samples with replacement
        for(int k = 0; k < SAMPLE_SIZE; k++) {
            int sample = ran.nextInt(MAX - MIN) + MIN;

            samples[k] = sample;
        }
    }
}
