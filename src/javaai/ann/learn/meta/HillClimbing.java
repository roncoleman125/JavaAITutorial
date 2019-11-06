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
 * This class implements the hill climbing unsupervised learning algorithm to find base point (5, 3).
 * I modified it from the Wikipedia algorithm to include an enhanced convergence test.
 * @author Ron.Coleman
 * @see <a href="https://en.wikipedia.org/wiki/Hill_climbing">HillClimbing climbing</a>
 */
public class HillClimbing {
    /** This is the tolerance below which hill climbing stops. */
    public final double TOLERANCE = 0.001;

    /** Maximum same count below the tolerance threshold */
    public final int MAX_SAME_COUNT = 5;

    /** Constraint on search space upper limit */
    public final double MAX = 10.0;

    /** Constraint on search space lower limit */
    public final double MIN = -10.0;

    /** Goal point */
    public final static double[] XY_GOAL = {5, 3};

    /** Current point updated as the climb progresses */
    protected double[] curPt = {0, 0};

    /** Step size in each dimension, X, Y, updated as the climb progresses */
    protected double[] stepSize = {1, 1};

    /** Baseline acceleration */
    final double acceleration = 1.2;

    /** Candidate accelerations to explore **/
    final double[] candidates = {-acceleration, -1/acceleration, 0, 1/acceleration, acceleration};

    /** Current same count, updated as the climbing progresses. */
    protected int sameCount = 0;

    /** Improvement updated as climb progresses */
    protected double improvement = Double.MAX_VALUE;

    public static void main(String[] args) {
        HillClimbing hill = new HillClimbing();

        double[] pt = hill.climb();

        System.out.println("CONVERED best: x="+pt[0]+" y="+pt[1]+" RMSE="+hill.getFitness(pt));
    }

    /**
     * Climbs the hill.
     * @return Destination
     */
    public double[] climb() {
        System.out.println("Hill climbing");
        System.out.printf("%3s %7s %7s %7s %7s %s\n","#","RMSE","x","y","improve","same");
        int iteration = 1;

        curPt = getStart();

        do {
            // Fitness before any moves
            double priorFitness = getFitness(curPt);

            // Test each dimension of the current point.
            for(int i=0; i < curPt.length; i++) {
                // Best candidate index so far -- unknown
                int bestIndex = -1;

                // If minimizing, we should be descending from here
                double bestFitness = Double.MAX_VALUE;

                // Try each candidate acceleration
                for(int j=0; j < candidates.length; j++) {
                    // Update the point in the ith direction we're moving
                    curPt[i] += stepSize[i]* candidates[j];

                    // Get its fitness
                    double newFitness = getFitness(curPt);

                    // Move the point back
                    curPt[i] -= stepSize[i]* candidates[j];

                    // If we improvement, remember this acceleration
                    if(newFitness < bestFitness) {
                        bestFitness = newFitness;
                        bestIndex = j;
                    }
                }

                // If we didn't improve, reduce the step size in this ith dimension
                // Note: best != -1 guaranteed since the best score (see above) is initially infinite.
                if(candidates[bestIndex] == 0)
                    stepSize[i] /= acceleration;

                // If we improvement, use the best acceleration we identified and
                // update the step in that ith dimension.
                else {
                    curPt[i] += stepSize[i]* candidates[bestIndex];
                    stepSize[i] *= candidates[bestIndex];
                }
            }

            // Test for convergence
            double curFitness = getFitness(curPt);

            improvement = Math.abs(curFitness - priorFitness);

            System.out.printf("%3d %7.4f %7.4f %7.4f %7.4f %d\n",iteration,curFitness,curPt[0],curPt[1], improvement,sameCount);

            if(didConverge())
                break;

            iteration++;

        } while(true);

        return curPt;
    }

    /**
     * Implements the objective function.
     * @param pt Point in N-space we're testing
     * @return RMSE
     */
    public double getFitness(double[] pt) {
        // Compute the um of square error
        double dist2 = 0;

        for(int k=0; k < pt.length; k++) {
            double delta = pt[k] - XY_GOAL[k];

            dist2 += (delta * delta);
        }

        return Math.sqrt(dist2/pt.length);
    }

    /**
     * Tests whether the climb converged.
     * @return True if we converged, false otherwise
     */
    public boolean didConverge() {

        if(improvement < TOLERANCE) {
            if(sameCount >= MAX_SAME_COUNT)
                return true;
            sameCount++;
        }
        else
            sameCount = 1;

        return false;
    }

    /**
     * Gets a random start point.
     * @return
     */
    protected double[] getStart() {
        Random ran = new Random(0);

        double[] pt = new double[curPt.length];

        for(int k=0; k < curPt.length; k++)
            pt[k] = ran.nextDouble() *(MAX-MIN) + MIN;

        return pt;
    }
}
