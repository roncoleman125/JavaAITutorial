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

import java.util.Random;
import static javaai.metah.hill.Direction.NONE;
import static javaai.metah.hill.Direction.MINIMIZE;

/**
 * This class implements the hill climbing reinforcement learning algorithm to find base point (5, 3).
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
    public final double CONSTRAINT_MAX = 10.0;

    /** Constraint on search space lower limit */
    public final double CONSTRAINT_MIN = -10.0;

    /** Goal point */
    public final static double[] XY_GOAL = {5, 3};

    /** Current point updated as the climb progresses */
    protected double[] curPt = {0, 0};

    /** Step size in each dimension, X, Y, updated as the climb progresses */
    protected double[] stepSize = {1, 1};

    /** Baseline acceleration */
    final double ACCELERATION = 1.2;

    /** Candidate accelerations to explore **/
    final double[] CANDIDATES = {-ACCELERATION, -1/ACCELERATION, 0, 1/ACCELERATION, ACCELERATION};

    /** Current same count, updated as the climbing progresses. */
    protected int sameCount = 0;

    /** Improvement updated as climb progresses */
    protected double improvement = Double.MAX_VALUE;

    /** Direction of climbing */
    protected Direction direction = NONE;

    /**
     * Launch point
     * @param args Command line args not used.
     */
    public static void main(String[] args) {
        HillClimbing hill = new HillClimbing(MINIMIZE);

        double[] pt = hill.climb();

        System.out.println("CONVERED best: x="+pt[0]+" y="+pt[1]+" RMSE="+hill.getFitness(pt));
    }

    /**
     * Constructor
     * @param direction Climb direction, MINIMIZE minimize, MAXIMIZE maximize
     */
    public HillClimbing(Direction direction) {
        this.direction = direction;
    }

    /**
     * Climbs the hill.
     * @return Destination
     */
    public double[] climb() {
        assert(direction != NONE);

        System.out.println("Hill climbing");
        System.out.printf("%3s %7s %7s %7s %7s %s\n","#","Fitness","x","y","improve","same");

        int iteration = 1;

        curPt = getStart();

        do {
            // Fitness before any moves
            double priorFitness = getFitness(curPt);

            // Test each dimension of the current point.
            for(int dim=0; dim < curPt.length; dim++) {
                // Best candidate index so far -- unknown
                int bestIdx = -1;

                // If minimizing, we should be descending from here
                double bestFitness = direction == MINIMIZE ? Double.MAX_VALUE : -Double.MAX_VALUE;

                // Try each candidate acceleration
                for(int idx = 0; idx < CANDIDATES.length; idx++) {
                    // Move point forward in ith direction we've been moving
                    curPt[dim] += stepSize[dim] * CANDIDATES[idx];

                    // Get current point fitness
                    double newFitness = getFitness(curPt);

                    // Move point back: we don't yet know if this point works
                    curPt[dim] -= stepSize[dim] * CANDIDATES[idx];

                    // If we improved, remember this candidate acceleration
                    if((direction == Direction.MINIMIZE && newFitness < bestFitness) ||
                       (direction == Direction.MAXIMIZE && newFitness > bestFitness)) {
                        bestFitness = newFitness;
                        bestIdx = idx;
                    }
                }

                // If we didn't improve, reduce the step size in this ith dimension
                // Note: best != -1 guaranteed since the best score (see above) is initially infinite.
                assert(bestIdx >= 0);

                if(CANDIDATES[bestIdx] == 0)
                    stepSize[dim] /= ACCELERATION;

                // If we improved, use the best acceleration we identified and
                // update the step in that ith dimension.
                else {
                    // Move point forward (again)
                    curPt[dim] += stepSize[dim] * CANDIDATES[bestIdx];

                    // Refine the step for the direction we improved
                    stepSize[dim] *= CANDIDATES[bestIdx];
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
        Random ran = new Random();

        double[] pt = new double[curPt.length];

        for(int k=0; k < curPt.length; k++)
            pt[k] = ran.nextDouble() *(CONSTRAINT_MAX - CONSTRAINT_MIN) + CONSTRAINT_MIN;

        return pt;
    }
}
